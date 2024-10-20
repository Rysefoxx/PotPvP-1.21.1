package io.potpvp.minecraft.core;

import io.potpvp.minecraft.util.ItemBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Constants for the core module.
 *
 * @author Rysefoxx
 * @since 18.10.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CoreConstants {

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Database {
    public static final String ENTITY_REMOVAL_ERROR = "ENTITY_REMOVAL_ERROR";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class General {
    public static final String SEPARATOR = " <gradient:#d3d3d3:#696969>✦</gradient> ";
    public static final String PREFIX = "<gradient:#1e90ff:#00ced1>[POT</gradient><gradient:#ff7f50:#ff4500>PVP]</gradient>{0}".interpolate(SEPARATOR);
    public static final String PERMISSION_PREFIX = "potpvp.";

    public static final String ENABLED = "ENABLED";
    public static final String DISABLED = "DISABLED";
    public static final String NO_PERMISSION = "NO_PERMISSION";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Command {
    public static final String LANGUAGE_INVALID = "LANGUAGE_INVALID";
    public static final String LANGUAGE_SELECTED = "LANGUAGE_SELECTED";
    public static final String USAGE = "USAGE";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Inventory {
    public static final ItemStack BORDER = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).displayName("§f").build();
    public static final String FIRST_PAGE = "FIRST_PAGE";
    public static final String FIRST_PAGE_INFO = "FIRST_PAGE_INFO";
    public static final String LAST_PAGE = "LAST_PAGE";
    public static final String LAST_PAGE_INFO = "LAST_PAGE_INFO";
    public static final String PAGE = "PAGE";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Service {
    public static final String WHITELIST_TOGGLE = "WHITELIST_TOGGLE";
    public static final String WHITELIST_ADD = "WHITELIST_ADD";
    public static final String WHITELIST_REMOVE = "WHITELIST_REMOVE";

    public static final String WHITELIST_ALREADY_ADDED = "WHITELIST_ALREADY_ADDED";
    public static final String WHITELIST_NOT_ADDED = "WHITELIST_NOT_ADDED";
  }


}