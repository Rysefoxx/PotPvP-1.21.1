package io.potpvp.minecraft.util;

import static io.potpvp.minecraft.core.CoreConstants.Inventory.FIRST_PAGE;
import static io.potpvp.minecraft.core.CoreConstants.Inventory.FIRST_PAGE_INFO;
import static io.potpvp.minecraft.core.CoreConstants.Inventory.LAST_PAGE;
import static io.potpvp.minecraft.core.CoreConstants.Inventory.LAST_PAGE_INFO;
import static io.potpvp.minecraft.core.CoreConstants.Inventory.PAGE;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.potpvp.minecraft.core.CorePlayer;
import io.potpvp.minecraft.keys.InventoryKeys;
import javax.annotation.Nonnegative;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * This class provides utility methods for inventories.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InventoryUtil {

  /**
   * Adds the previous page item to the inventory.
   *
   * @param contents   The inventory contents.
   * @param pagination The pagination.
   * @param corePlayer The core player.
   * @param row        The row.
   * @param column     The column.
   */
  @SuppressWarnings("checkstyle:Indentation")
  public static void previous(@NotNull InventoryContents contents,
                              @NotNull Pagination pagination,
                              @NotNull CorePlayer corePlayer,
                              @Nonnegative int row,
                              @Nonnegative int column) {

    int previousPage = pagination.page() - 1;
    contents.set(row, column, IntelligentItem.of(ItemBuilder.of(Material.PAPER)
        .dataContainer(NamespacedKey.minecraft(InventoryKeys.INVENTORY_PREVIOUS), PersistentDataType.STRING, "")
        .amount(pagination.isFirst()
            ? 1
            : pagination.page() - 1)
        .displayName(pagination.isFirst()
            ? FIRST_PAGE.translate(corePlayer.getUuid()).toComponent().orElseThrow()
            : PAGE.translate(corePlayer.getUuid(), previousPage).toComponent().orElseThrow()).build(), event -> {
      if (!event.isLeftClick() && !event.isRightClick()) {
        return;
      }
      if (pagination.isFirst()) {
        corePlayer.info(FIRST_PAGE_INFO.translate(corePlayer.getUuid()));
        corePlayer.sound(Sound.BLOCK_ANVIL_BREAK);
        return;
      }
      corePlayer.soundUiClick();

      RyseInventory currentInventory = pagination.inventory();
      currentInventory.open(corePlayer.getPlayer(), pagination.previous().page());
    }));
  }

  /**
   * Adds the next page item to the inventory.
   *
   * @param contents   The inventory contents.
   * @param pagination The pagination.
   * @param corePlayer The core player.
   * @param row        The row.
   * @param column     The column.
   */
  @SuppressWarnings("checkstyle:Indentation")
  public static void next(@NotNull InventoryContents contents,
                          @NotNull Pagination pagination,
                          @NotNull CorePlayer corePlayer,
                          @Nonnegative int row,
                          @Nonnegative int column) {

    int page = pagination.page() + 1;
    contents.set(row, column, IntelligentItem.of(ItemBuilder.of(Material.PAPER)
        .dataContainer(NamespacedKey.minecraft(InventoryKeys.INVENTORY_NEXT), PersistentDataType.STRING, "")
        .amount((pagination.isLast() ? 1 : page))
        .displayName(pagination.isLast()
            ? LAST_PAGE.translate(corePlayer.getUuid()).toComponent().orElseThrow()
            : PAGE.translate(corePlayer.getUuid(), page).toComponent().orElseThrow())
        .build(), event -> {
      if (!event.isLeftClick() && !event.isRightClick()) {
        return;
      }
      if (pagination.isLast()) {
        corePlayer.info(LAST_PAGE_INFO.translate(corePlayer.getUuid()));
        corePlayer.sound(Sound.BLOCK_ANVIL_BREAK);
        return;
      }

      corePlayer.soundUiClick();

      RyseInventory currentInventory = pagination.inventory();
      currentInventory.open(corePlayer.getPlayer(), pagination.next().page());
    }));
  }

}