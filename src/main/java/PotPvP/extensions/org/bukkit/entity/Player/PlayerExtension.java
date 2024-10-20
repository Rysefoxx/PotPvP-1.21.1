package PotPvP.extensions.org.bukkit.entity.Player;

import io.potpvp.minecraft.PotPlugin;
import io.potpvp.minecraft.core.CorePlayer;
import io.potpvp.minecraft.service.core.CorePlayerService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.bukkit.entity.Player;

/**
 * Extension methods for the {@link Player} class.
 *
 * @author Rysefoxx
 * @since 14.10.2024
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Extension
public class PlayerExtension {

  private static final CorePlayerService CORE_PLAYER_SERVICE = PotPlugin.getContext().getBean(CorePlayerService.class);

  /**
   * Converts the given {@link Player} to a {@link CorePlayer}.
   *
   * @param player The player to convert.
   * @return The converted player.
   */
  public static CorePlayer toCorePlayer(@This Player player) {
    return CORE_PLAYER_SERVICE.fetchPlayer(player.getUniqueId());
  }
}