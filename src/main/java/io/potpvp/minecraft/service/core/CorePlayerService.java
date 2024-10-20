/*
 *      Copyright (c) 2023 Rysefoxx
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package io.potpvp.minecraft.service.core;

import io.potpvp.minecraft.PotPlugin;
import io.potpvp.minecraft.core.CorePlayer;
import io.potpvp.minecraft.event.SystemInitializedEvent;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * This class represents a service that handles players.
 *
 * @author Rysefoxx
 * @since 30.10.2023
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CorePlayerService {

  private final PotPlugin plugin;
  private final ConcurrentHashMap<UUID, CorePlayer> corePlayerMap = new ConcurrentHashMap<>();

  /**
   * Fetches a player by its UUID.
   *
   * @param uuid The UUID of the player.
   * @return The player.
   */
  public @NotNull CorePlayer fetchPlayer(@NotNull UUID uuid) {
    return this.corePlayerMap.computeIfAbsent(uuid,
        key -> new CorePlayer(key, this.plugin));
  }

  /**
   * Clean the player cache.
   */
  @EventListener(SystemInitializedEvent.class)
  public void clean() {
    Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
      PotPlugin.getLog().warning("Cleaning player cache...");
      PotPlugin.getLog().warning("Entries before: " + this.corePlayerMap.size());
      for (Map.Entry<UUID, CorePlayer> entry : this.corePlayerMap.entrySet()) {
        CorePlayer corePlayer = entry.getValue();
        if (corePlayer.isOnline()) {
          continue;
        }

        this.corePlayerMap.remove(entry.getKey());
      }
      PotPlugin.getLog().warning("Entries after: " + this.corePlayerMap.size());
    }, 0L, 20L * 60L);
  }
}
