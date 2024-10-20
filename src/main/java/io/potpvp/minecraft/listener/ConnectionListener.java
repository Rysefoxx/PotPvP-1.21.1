package io.potpvp.minecraft.listener;

import io.potpvp.minecraft.entity.player.PlayerEntity;
import io.potpvp.minecraft.service.player.PlayerService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Rysefoxx
 * @since 19.10.2024
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ConnectionListener implements Listener {

  private final PlayerService playerService;

  @EventHandler
  public void onJoin(@NotNull PlayerJoinEvent event) {
    Player player = event.getPlayer();
    Optional<PlayerEntity> optional = this.playerService.findByUuid(player.getUniqueId());
    if (optional.isEmpty()) {
      this.playerService.create(player);
      return;
    }

    this.playerService.tryUpdatePlayerName(player);
  }

}