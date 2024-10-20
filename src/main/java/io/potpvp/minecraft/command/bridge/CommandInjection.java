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

import io.potpvp.minecraft.PotPlugin;
import io.potpvp.minecraft.command.bridge.annotations.MainCommand;
import io.potpvp.minecraft.event.SystemInitializedEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for injecting commands into the server.
 *
 * @author Rysefoxx
 * @since 30.10.2023
 */
@Component
public class CommandInjection {

  /**
   * This method is responsible for registering all commands.
   *
   * @param event the event that is triggered when the plugin is initialized
   */
  @EventListener(SystemInitializedEvent.class)
  public void registration(@NotNull SystemInitializedEvent event) {
    ConfigurableApplicationContext context = PotPlugin.getContext();

    context.getBeansOfType(SubCommand.class).forEach((s, subCommand) -> registerCommand(event.getPlugin(), subCommand));
  }

  private void registerCommand(@NotNull Plugin plugin, @NotNull SubCommand command) {
    MainCommand mainCommand = command.mainCommand();

    if (mainCommand == null) {
      PotPlugin.getLog().log(Level.SEVERE, "Could not register Command " + command.getClass().getSimpleName() + " because it has no MainCommand annotation");
      return;
    }

    PluginCommand pluginCommand = getCommand(mainCommand.name(), plugin);

    if (pluginCommand == null) {
      PotPlugin.getLog().log(Level.SEVERE, "Could not register Command " + mainCommand.name());
      return;
    }

    pluginCommand.setExecutor(command);

    if (mainCommand.alias() != null) {
      pluginCommand.setAliases(Arrays.asList(mainCommand.alias()));
    }

    pluginCommand.setDescription("potera");

    CommandMap map = getCommandMap(plugin);

    if (map == null) {
      PotPlugin.getLog().log(Level.SEVERE, "Could not register Command " + mainCommand.name());
      return;
    }

    map.register(plugin.getName(), pluginCommand);
  }

  private @Nullable PluginCommand getCommand(@NotNull String name, @NotNull Plugin plugin) {
    PluginCommand command;

    try {
      Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
      constructor.setAccessible(true);

      command = constructor.newInstance(name, plugin);
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException
             | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
      PotPlugin.getLog().log(Level.SEVERE, "Could not register Command " + name);
      return null;
    }

    return command;
  }

  @SuppressWarnings("removal")
  private @Nullable CommandMap getCommandMap(@NotNull Plugin plugin) {
    CommandMap commandMap = null;

    try {
      if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
        Field field = SimplePluginManager.class.getDeclaredField("commandMap");
        field.setAccessible(true);

        commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
      }
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      PotPlugin.getLog().log(Level.SEVERE, "Could not register Command " + plugin.getName());
      return null;
    }

    return commandMap;
  }
}
