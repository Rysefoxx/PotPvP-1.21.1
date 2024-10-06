package io.potpvp.minecraft;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class of the PotPvP plugin.
 *
 * @author Rysefoxx
 * @since 06.10.2024
 */
public class PotPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    getLogger().info("PotPvP Plugin enabled!");
  }

  @Override
  public void onDisable() {
    getLogger().info("PotPvP Plugin disabled!");
  }
}