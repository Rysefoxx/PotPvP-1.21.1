package io.potpvp.minecraft.listener.core;

import io.potpvp.minecraft.service.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The listener class for handling connection events.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ConnectionListener implements Listener {

  private final PlayerService playerService;

  @EventHandler(priority = EventPriority.LOW)
  public void onJoin(@NotNull PlayerJoinEvent event) {
    Player player = event.getPlayer();
    this.playerService.playerJoin(player);
  }

}