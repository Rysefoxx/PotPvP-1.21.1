package io.potpvp.minecraft.service.whitelist;

import io.potpvp.minecraft.entity.whitelist.WhitelistEntity;
import io.potpvp.minecraft.event.SystemInitializedEvent;
import io.potpvp.minecraft.factory.EntityProxyFactory;
import io.potpvp.minecraft.repository.whitelist.WhitelistRepository;
import io.potpvp.minecraft.service.EntityService;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
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
public class WhitelistService implements EntityService<WhitelistEntity> {

  private final WhitelistRepository repository;
  private final Set<WhitelistEntity> cache = ConcurrentHashMap.newKeySet();

  @Override
  public void delete(@NotNull WhitelistEntity toDelete) {
    CompletableFuture.runAsync(() -> this.repository.deleteById(toDelete.getUuid()));
    this.cache.remove(toDelete);
  }

  @Override
  public void load(@NotNull SystemInitializedEvent event) {
    this.repository.findAll().forEach(whitelistEntity -> this.cache.add(EntityProxyFactory.createProxy(whitelistEntity, this.repository)));
  }

  /**
   * Adds a user to the whitelist.
   *
   * @param uuid the uuid of the user to add
   */
  public void addUser(@NotNull UUID uuid) {
    WhitelistEntity whitelistEntity = EntityProxyFactory.createProxy(WhitelistEntity.class, this.repository);
    whitelistEntity.setUuid(uuid);
    this.cache.add(whitelistEntity);
  }

  /**
   * Removes a user from the whitelist.
   *
   * @param uuid the uuid of the user to remove
   */
  public void removeUser(@NotNull UUID uuid) {
    getWhitelistEntity(uuid).ifPresent(this::delete);
  }

  /**
   * Get a {@link WhitelistEntity} by uuid.
   *
   * @param uuid the uuid to get the entity for
   * @return the entity if present
   */
  public Optional<WhitelistEntity> getWhitelistEntity(@NotNull UUID uuid) {
    return this.cache.stream()
        .filter(whitelistEntity -> whitelistEntity.getUuid().equals(uuid))
        .findFirst();
  }
}