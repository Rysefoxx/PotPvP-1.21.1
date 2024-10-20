package io.potpvp.minecraft.inventory.impl;

import static io.potpvp.minecraft.core.CoreConstants.Database.ENTITY_REMOVAL_ERROR;
import static io.potpvp.minecraft.core.CoreConstants.General.PREFIX;
import static io.potpvp.minecraft.core.CoreConstants.Inventory.BORDER;
import static io.potpvp.minecraft.core.CoreConstants.Service.WHITELIST_REMOVE;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import io.potpvp.minecraft.core.CorePlayer;
import io.potpvp.minecraft.database.ResultHandler;
import io.potpvp.minecraft.entity.whitelist.WhitelistEntity;
import io.potpvp.minecraft.inventory.AbstractInventory;
import io.potpvp.minecraft.service.whitelist.WhitelistService;
import io.potpvp.minecraft.util.InventoryUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * This class represents the whitelist list inventory.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
public class WhitelistListInventory extends AbstractInventory {

  private static final int ROWS = 6;
  private final WhitelistService whitelistService;

  public WhitelistListInventory(Plugin plugin, WhitelistService whitelistService) {
    super(plugin, "WHITELIST_TITLE", ROWS);
    this.whitelistService = whitelistService;
    setProvider(new Provider());
  }

  private class Provider implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {
      CorePlayer corePlayer = player.toCorePlayer();
      contents.fillBorders(BORDER);

      Pagination pagination = contents.pagination();
      pagination.setItemsPerPage(28);
      pagination.iterator(SlotIterator.builder()
          .type(SlotIterator.SlotIteratorType.HORIZONTAL)
          .startPosition(1, 1)
          .build());

      InventoryUtil.previous(contents, pagination, corePlayer, 5, 3);

      for (WhitelistEntity entity : whitelistService.getCache()) {
        pagination.addItem(IntelligentItem.of(whitelistService.getDisplayItem(entity, corePlayer.getUuid()), clickEvent -> {
          whitelistService.removeUser(entity.getUuid(), new ResultHandler<>() {
            @Override
            public void success(WhitelistEntity entity) {
              corePlayer.translate(WHITELIST_REMOVE, PREFIX, entity.getUuid().getNameOrDefault());
              contents.removeItemWithConsumer(clickEvent.getSlot());
              corePlayer.soundUiClick();
            }

            @Override
            public void error(WhitelistEntity entity, Throwable throwable) {
              ResultHandler.super.error(entity, throwable);
              corePlayer.translate(ENTITY_REMOVAL_ERROR);
              corePlayer.closeInventory();
            }
          });
        }));
      }

      InventoryUtil.next(contents, pagination, corePlayer, 5, 5);
    }
  }
}