package io.potpvp.minecraft.service.whitelist;

import io.potpvp.minecraft.core.CorePlayer;
import io.potpvp.minecraft.database.ResultHandler;
import io.potpvp.minecraft.entity.whitelist.WhitelistEntity;
import io.potpvp.minecraft.service.EntityService;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * This service manages the whitelist.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
public interface WhitelistServiceLogic extends EntityService<WhitelistEntity> {

  /**
   * Adds a user to the whitelist.
   *
   * @param uuid the uuid of the user to add
   */
  void addUser(@NotNull UUID uuid);

  /**
   * Removes a user from the whitelist.
   *
   * @param uuid          the uuid of the user to remove
   * @param resultHandler the result handler
   */
  void removeUser(@NotNull UUID uuid, @NotNull ResultHandler<WhitelistEntity> resultHandler);

  /**
   * Toggles the whitelist.
   *
   * @param corePlayer the player who toggles the whitelist
   */
  void toggle(@NotNull CorePlayer corePlayer);

  /**
   * Lists all whitelisted players.
   *
   * @param corePlayer the player who lists the whitelist
   */
  void list(@NotNull CorePlayer corePlayer);

  /**
   * Gets the {@link WhitelistEntity} by the given {@link UUID}.
   *
   * @return the {@link WhitelistEntity} by the given {@link UUID}
   */
  @NotNull
  Set<WhitelistEntity> getCache();

  /**
   * Gets the {@link WhitelistEntity} by the given {@link UUID}.
   *
   * @param entity          the entity to get the display item for
   * @param translationUuid user uuid to get the language for translation
   * @return the {@link ItemStack} for the given {@link WhitelistEntity}
   */
  @NotNull
  ItemStack getDisplayItem(@NotNull WhitelistEntity entity, @NotNull UUID translationUuid);

  /**
   * Get a {@link WhitelistEntity} by uuid.
   *
   * @param uuid the uuid to get the entity for
   * @return the entity if present
   */
  Optional<WhitelistEntity> getWhitelistEntity(@NotNull UUID uuid);

  /**
   * Checks if a player is whitelisted.
   *
   * @param uuid the uuid to check
   * @return true if the player is whitelisted
   */
  boolean isWhitelisted(@NotNull UUID uuid);

  /**
   * Checks if the whitelist is enabled.
   *
   * @return true if the whitelist is enabled
   */
  boolean isEnabled();

}