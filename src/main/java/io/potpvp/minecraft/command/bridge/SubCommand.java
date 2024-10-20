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

package io.potpvp.minecraft.command.bridge;

import io.potpvp.minecraft.command.bridge.annotations.MainCommand;
import io.potpvp.minecraft.core.CorePlayer;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a sub command.
 *
 * @author Rysefoxx
 * @since 14.10.2024
 */
@Getter
@Accessors(fluent = true)
@NoArgsConstructor
public abstract class SubCommand implements CommandExecutor, TabCompleter {

  @Nullable
  private final MainCommand mainCommand = getClass().getAnnotation(MainCommand.class);

  /**
   * Executes the given command, returning its success.
   * <br>
   * If false is returned, then the "usage" plugin.yml entry for this command
   * (if defined) will be sent to the player.
   *
   * @param sender  Source of the command
   * @param command Command which was executed
   * @param label   Alias of the command which was used
   * @param args    Passed command arguments
   * @return true if a valid command, otherwise false
   */
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    execute(sender, sender instanceof Player player ? player.toCorePlayer() : null, new CommandArguments(args));
    return false;
  }

  /**
   * This method is responsible for executing the command.
   *
   * @param sender     the sender of the command
   * @param corePlayer the {@link CorePlayer} of the sender
   * @param arguments  the arguments of the command
   */
  public abstract void execute(@NotNull CommandSender sender, @Nullable CorePlayer corePlayer, @NotNull CommandArguments arguments);

  protected @Nullable List<String> tabComplete(@NotNull CorePlayer corePlayer, @NotNull CommandArguments arguments) {
    return null;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return null;
    }

    CorePlayer corePlayer = player.toCorePlayer();
    return tabComplete(corePlayer, new CommandArguments(args));
  }

}
