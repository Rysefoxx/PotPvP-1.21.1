package io.potpvp.minecraft;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import io.potpvp.minecraft.database.CompoundClassLoader;
import io.potpvp.minecraft.database.SpringApplication;
import io.potpvp.minecraft.database.SpringSpigotBootstrapper;
import io.potpvp.minecraft.event.SystemInitializedEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
public class PotPlugin extends JavaPlugin {

  @Getter
  private static ConfigurableApplicationContext context;
  @Getter
  private static Logger log;
  private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
  private static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.legacySection();

  private final InventoryManager inventoryManager = new InventoryManager(this);

  @Override
  public void onEnable() {
    log = getLogger();
    buildContext();
    registerEvents();

    eventPublisher();
    saveDefaultConfig();
    this.inventoryManager.invoke();

    getLogger().info("PotPvP Plugin enabled!");
  }

  @Override
  public void onDisable() {
    getLogger().info("PotPvP Plugin disabled!");

    if (context != null && context.isActive) {
      context.close();
    }
  }

  private void registerEvents() {
    context.getBeansOfType(Listener.class).values().forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
  }

  private void buildContext() {
    List<ClassLoader> classLoaders = new ArrayList<>();
    classLoaders.add(0, getClass().getClassLoader());
    classLoaders.add(1, Thread.currentThread().getContextClassLoader());

    CompoundClassLoader loader = new CompoundClassLoader(classLoaders);
    context = SpringSpigotBootstrapper.initialize(this, loader, SpringApplication.class);
  }

  public static MiniMessage getMiniMessage() {
    return MINI_MESSAGE;
  }

  public static LegacyComponentSerializer getLegacyComponentSerializer() {
    return LEGACY_COMPONENT_SERIALIZER;
  }

  private void eventPublisher() {
    context.publishEvent(new SystemInitializedEvent(this));
  }
}