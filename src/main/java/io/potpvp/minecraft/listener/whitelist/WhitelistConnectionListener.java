package io.potpvp.minecraft.listener.whitelist;

import static io.potpvp.minecraft.core.CoreConstants.Listener.NOT_WHITELISTED;

import io.potpvp.minecraft.service.whitelist.WhitelistService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is a listener for the {@link AsyncPlayerPreLoginEvent} and handles the whitelist.
 *
 * @author Rysefoxx
 * @since 20.10.2024
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WhitelistConnectionListener implements Listener {

  private final WhitelistService whitelistService;

  @EventHandler(priority = EventPriority.LOWEST)
  public void onAsyncJoin(@NotNull AsyncPlayerPreLoginEvent event) {
    UUID uuid = event.getUniqueId();
    if (this.whitelistService.isWhitelisted(uuid)) {
      return;
    }

    boolean enabled = this.whitelistService.isEnabled();
    if (!enabled) {
      return;
    }

    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, NOT_WHITELISTED.translate(uuid).toComponent().orElseThrow());
  }
}