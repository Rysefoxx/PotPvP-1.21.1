package PotPvP.extensions.java.util.UUID;

import io.potpvp.minecraft.PotPlugin;
import io.potpvp.minecraft.entity.player.PlayerEntity;
import io.potpvp.minecraft.service.player.PlayerService;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.jetbrains.annotations.NotNull;


/**
 * Extension methods for the {@link UUID} class.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Extension
public class UUIDExtension {

  private static final PlayerService PLAYER_SERVICE = PotPlugin.getContext().getBean(PlayerService.class);

  /**
   * Returns the name of the player with the given uuid or "Unknown" if the player is not found.
   *
   * @param uuid the uuid of the player
   * @return the name of the player or "Unknown"
   */
  public static @NotNull String getNameOrDefault(@This UUID uuid) {
    Optional<PlayerEntity> optional = PLAYER_SERVICE.findByUuid(uuid);
    if (optional.isEmpty()) {
      return "Unknown";
    }

    return optional.get().getPlayerName();
  }

}