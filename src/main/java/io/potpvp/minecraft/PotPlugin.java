package io.potpvp.minecraft;

import io.potpvp.minecraft.database.CompoundClassLoader;
import io.potpvp.minecraft.database.SpringApplication;
import io.potpvp.minecraft.database.SpringSpigotBootstrapper;
import io.potpvp.minecraft.event.SystemInitializedEvent;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main class of the PotPvP plugin.
 *
 * @author Rysefoxx
 * @since 06.10.2024
 */
@Getter
public class PotPlugin extends JavaPlugin {

  private ConfigurableApplicationContext context;

  @Override
  public void onEnable() {
    buildContext();
    registerEvents();

    eventPublisher();
    getLogger().info("PotPvP Plugin enabled!");
  }

  @Override
  public void onDisable() {
    getLogger().info("PotPvP Plugin disabled!");

    if (this.context != null && this.context.isActive) {
      this.context.close();
      this.context = null;
    }
  }

  private void registerEvents() {
    this.context.getBeansOfType(Listener.class).values().forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
  }

  private void buildContext() {
    List<ClassLoader> classLoaders = new ArrayList<>();
    classLoaders.add(0, getClass().getClassLoader());
    classLoaders.add(1, Thread.currentThread().getContextClassLoader());

    CompoundClassLoader loader = new CompoundClassLoader(classLoaders);
    this.context = SpringSpigotBootstrapper.initialize(this, loader, SpringApplication.class);
  }

  private void eventPublisher() {
    this.context.publishEvent(new SystemInitializedEvent(this));
  }
}