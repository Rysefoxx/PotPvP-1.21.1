package io.potpvp.minecraft.service.whitelist;

import static io.potpvp.minecraft.core.CoreConstants.General.DISABLED;
import static io.potpvp.minecraft.core.CoreConstants.General.ENABLED;
import static io.potpvp.minecraft.core.CoreConstants.General.PREFIX;
import static io.potpvp.minecraft.core.CoreConstants.Service.WHITELIST_ADD;
import static io.potpvp.minecraft.core.CoreConstants.Service.WHITELIST_ALREADY_ADDED;
import static io.potpvp.minecraft.core.CoreConstants.Service.WHITELIST_NOT_ADDED;
import static io.potpvp.minecraft.core.CoreConstants.Service.WHITELIST_TOGGLE;

import io.potpvp.minecraft.PotPlugin;
import io.potpvp.minecraft.core.CorePlayer;
import io.potpvp.minecraft.database.ResultHandler;
import io.potpvp.minecraft.entity.whitelist.WhitelistEntity;
import io.potpvp.minecraft.event.SystemInitializedEvent;
import io.potpvp.minecraft.factory.EntityProxyFactory;
import io.potpvp.minecraft.inventory.impl.WhitelistListInventory;
import io.potpvp.minecraft.repository.whitelist.WhitelistRepository;
import io.potpvp.minecraft.util.ItemBuilder;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service manages the whitelist.
 *
 * @author Rysefoxx
 * @since 06.10.2024
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WhitelistService implements WhitelistServiceLogic {

  private final PotPlugin plugin;
  private final WhitelistRepository repository;
  private final Set<WhitelistEntity> cache = ConcurrentHashMap.newKeySet();

  @Override
  public void delete(@NotNull WhitelistEntity toDelete, @NotNull ResultHandler<WhitelistEntity> resultHandler) {
    CompletableFuture.runAsync(() -> {
      try {
        this.repository.deleteById(toDelete.getUuid());
        this.cache.remove(toDelete);
        resultHandler.success(toDelete);
      } catch (Exception exception) {
        resultHandler.error(toDelete, exception);
      }
    });
  }

  @Override
  public void load(@NotNull SystemInitializedEvent event) {
    this.repository.findAll().forEach(entity -> this.cache.add(EntityProxyFactory.createProxy(entity, this.repository)));
  }

  @Override
  public void addUser(@NotNull UUID uuid) {
    WhitelistEntity entity = EntityProxyFactory.createProxy(WhitelistEntity.class, this.repository);
    entity.setUuid(uuid);
    this.cache.add(entity);
  }

  @Override
  public void removeUser(@NotNull UUID uuid, @NotNull ResultHandler<WhitelistEntity> resultHandler) {
    getWhitelistEntity(uuid).ifPresent(toDelete -> delete(toDelete, resultHandler));
  }

  @Override
  public void toggle(@NotNull CorePlayer corePlayer) {
    boolean enabled = !isEnabled();
    this.plugin.getConfig().set("enabled", enabled);
    this.plugin.saveConfig();

    corePlayer.translate(WHITELIST_TOGGLE, PREFIX, enabled ? ENABLED.translate(corePlayer.getUuid()) : DISABLED.translate(corePlayer.getUuid()));
  }

  @Override
  public void list(@NotNull CorePlayer corePlayer) {
    new WhitelistListInventory(this.plugin, this).open(corePlayer);
  }

  @Override
  public @NotNull Set<WhitelistEntity> getCache() {
    return this.cache;
  }

  @Override
  public @NotNull ItemStack getDisplayItem(@NotNull WhitelistEntity entity, @NotNull UUID translationUuid) {
    String name = entity.getUuid().getNameOrDefault();
    return ItemBuilder.of(Material.PLAYER_HEAD)
        .displayName("§a{0}".interpolate(name))
        .skullOwner(name)
        .lore("WHITELIST_LORE_1".translate(translationUuid))
        .build();
  }

  @Override
  public Optional<WhitelistEntity> getWhitelistEntity(@NotNull UUID uuid) {
    return this.cache.stream()
        .filter(whitelistEntity -> whitelistEntity.getUuid().equals(uuid))
        .findFirst();
  }

  @Override
  public boolean isWhitelisted(@NotNull UUID uuid) {
    return getWhitelistEntity(uuid).isPresent();
  }

  @Override
  public boolean isEnabled() {
    return this.plugin.getConfig().getBoolean("whitelist", true);
  }

  /**
   * Adds a user to the whitelist.
   *
   * @param executor   the executor of the command
   * @param targetName the name of the user to add
   */
  public void addUser(@NotNull CorePlayer executor, @NotNull String targetName) {
    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(targetName);

    if (offlinePlayer == null) {
      //Bukkit.getOfflinePlayer can be blocking, so we run it async
      CompletableFuture.runAsync(() -> handlePlayerAddition(executor, targetName, Bukkit.getOfflinePlayer(targetName)));
    } else {
      handlePlayerAddition(executor, targetName, offlinePlayer);
    }
  }

  private void handlePlayerAddition(@NotNull CorePlayer executor, @NotNull String targetName, @NotNull OfflinePlayer offlinePlayer) {
    if (isWhitelisted(offlinePlayer.getUniqueId())) {
      executor.translate(WHITELIST_ALREADY_ADDED, PREFIX, targetName);
      return;
    }

    addUser(offlinePlayer.getUniqueId());
    executor.translate(WHITELIST_ADD, PREFIX, targetName);
  }

  /**
   * Removes a user from the whitelist.
   *
   * @param executor      the executor of the command
   * @param targetName    the name of the user to remove
   * @param resultHandler the result handler for the removal
   */
  public void removeUser(@NotNull CorePlayer executor, @NotNull String targetName, @NotNull ResultHandler<WhitelistEntity> resultHandler) {
    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(targetName);

    if (offlinePlayer == null) {
      //Bukkit.getOfflinePlayer can be blocking, so we run it async
      CompletableFuture.runAsync(() -> handlePlayerRemoval(executor, targetName, Bukkit.getOfflinePlayer(targetName), resultHandler));
    } else {
      handlePlayerRemoval(executor, targetName, offlinePlayer, resultHandler);
    }
  }

  private void handlePlayerRemoval(@NotNull CorePlayer executor, @NotNull String targetName, @NotNull OfflinePlayer offlinePlayer, @NotNull ResultHandler<WhitelistEntity> resultHandler) {
    if (!isWhitelisted(offlinePlayer.getUniqueId())) {
      executor.translate(WHITELIST_NOT_ADDED, PREFIX, targetName);
      return;
    }

    removeUser(offlinePlayer.getUniqueId(), resultHandler);
  }
}