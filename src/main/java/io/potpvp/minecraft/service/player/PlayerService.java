package io.potpvp.minecraft.service.player;

import io.potpvp.minecraft.database.ResultHandler;
import io.potpvp.minecraft.entity.player.PlayerEntity;
import io.potpvp.minecraft.event.SystemInitializedEvent;
import io.potpvp.minecraft.factory.EntityProxyFactory;
import io.potpvp.minecraft.repository.player.PlayerRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service manages the player entries.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PlayerService implements PlayerServiceLogic {

  private final PlayerRepository repository;
  private final Set<PlayerEntity> cache = ConcurrentHashMap.newKeySet();

  @Override
  public void delete(@NotNull PlayerEntity toDelete, @NotNull ResultHandler<PlayerEntity> resultHandler) {
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
  public void create(@NotNull Player player) {
    PlayerEntity entity = EntityProxyFactory.createProxy(PlayerEntity.class, this.repository);
    entity.setUuid(player.getUniqueId());
    entity.setPlayerName(player.getName());
    this.cache.add(entity);
  }

  @Override
  public void tryUpdatePlayerName(@NotNull Player player) {
    Optional<PlayerEntity> optional = findByUuid(player.getUniqueId());
    if (optional.isEmpty()) {
      return;
    }

    PlayerEntity entity = optional.get();
    if (entity.getPlayerName().equals(player.getName())) {
      return;
    }

    entity.setPlayerName(player.getName());
  }

  @Override
  public Optional<PlayerEntity> findByUuid(@NotNull UUID uuid) {
    return this.cache.stream()
        .filter(entity -> entity.getUuid().equals(uuid))
        .findFirst();
  }

  @Override
  public void playerJoin(@NotNull Player player) {
    Optional<PlayerEntity> optional = findByUuid(player.getUniqueId());
    if (optional.isEmpty()) {
      create(player);
      return;
    }

    tryUpdatePlayerName(player);
  }
}