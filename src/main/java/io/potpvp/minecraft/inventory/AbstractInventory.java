package io.potpvp.minecraft.inventory;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.potpvp.minecraft.core.CorePlayer;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is the abstract class for all inventories.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@RequiredArgsConstructor
public abstract class AbstractInventory {

  protected final Plugin plugin;
  protected final String titleTranslationKey;
  protected final int rows;

  protected @Nullable InventoryProvider provider;

  /**
   * Opens the inventory for the given players
   *
   * @param players The players to open the inventory for.
   */
  public void open(@NotNull CorePlayer @NotNull ... players) {
    players.forEach((unused, o) -> {
      Component translatedTitle = this.titleTranslationKey.translate(o.getUuid()).toComponentOrFallback(Optional.of(Component.text("INVALID"))).orElseThrow();
      RyseInventory.builder()
          .title(translatedTitle)
          .rows(this.rows)
          .disableUpdateTask()
          .provider(getProvider())
          .build(this.plugin)
          .open(o.getPlayer());
    });
  }

  protected @NotNull InventoryProvider getProvider() {
    if (this.provider == null) {
      this.provider = new InventoryProvider() {
        @Override
        public void init(Player player, InventoryContents contents) {
          // Empty
        }
      };
    }

    return this.provider;
  }

  protected void setProvider(@NotNull InventoryProvider provider) {
    this.provider = provider;
  }
}