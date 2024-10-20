package io.potpvp.minecraft.service.player;

import io.potpvp.minecraft.entity.player.PlayerEntity;
import io.potpvp.minecraft.service.EntityService;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This service manages the player entries.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
public interface PlayerServiceLogic extends EntityService<PlayerEntity> {

  /**
   * Creates a new player entry.
   *
   * @param player the player to create the entry for
   */
  void create(@NotNull Player player);

  /**
   * Tries to update the player name.
   *
   * @param player the player to update the name for
   */
  void tryUpdatePlayerName(@NotNull Player player);

  /**
   * Finds a player by the given uuid.
   *
   * @param uuid the uuid to find the player by
   * @return the player if found
   */
  Optional<PlayerEntity> findByUuid(@NotNull UUID uuid);

}
