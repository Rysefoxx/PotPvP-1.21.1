package io.potpvp.minecraft.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.potpvp.minecraft.PotPlugin;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import javax.annotation.Nonnegative;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Builder for creating {@link ItemStack}s.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@NoArgsConstructor
@Setter
@Getter
public final class ItemBuilder {

  private static final UUID PROFILE_UUID = UUID.fromString("8667ba71-b85a-4004-af54-457a9734eed7");

  private ItemStack itemStack;

  private Component displayName;
  private String displayNameOld;
  private Material material;
  private int amount = 1;
  private ItemMeta itemMeta;

  private OfflinePlayer skullOwner;
  private String skullUrl;

  private List<ItemFlag> flags = new ArrayList<>();
  private List<Component> lore = new ArrayList<>();
  private List<String> loreOld = new ArrayList<>();
  private Map<Enchantment, Integer> enchantments = new HashMap<>();
  private Set<Container<?>> containers = new HashSet<>();
  private int customModelData = -1;
  private boolean unbreakable;

  private ItemBuilder(@NotNull ItemStack itemStack) {
    this.itemStack = itemStack;
    this.material = itemStack.getType();
    this.amount = itemStack.getAmount();
    this.itemMeta = itemStack.getItemMeta();

    if (this.itemMeta == null) {
      this.itemMeta = Bukkit.getItemFactory().getItemMeta(this.material);
    }

    if (this.itemMeta != null && this.itemMeta.hasLore()) {
      this.lore = this.itemMeta.lore();
    }

    if (this.itemMeta != null && this.itemMeta.hasEnchants()) {
      this.enchantments = this.itemMeta.getEnchants();
    }

    if (this.itemMeta != null && this.itemMeta.hasDisplayName()) {
      this.displayName = this.itemMeta.displayName();
    }

    if (this.itemMeta != null && !this.itemMeta.getItemFlags().isEmpty()) {
      this.flags = new ArrayList<>(Arrays.asList(this.itemMeta.getItemFlags().toArray(new ItemFlag[0])));
    }

    if (this.itemMeta instanceof SkullMeta skullMeta) {
      this.skullOwner = skullMeta.getOwningPlayer();
    }
  }

  private ItemBuilder(@NotNull Material material) {
    this.material = material;
  }

  private ItemBuilder(@NotNull Material material, @NotNull Component displayName) {
    this.material = material;
    this.displayName = displayName;
  }

  /**
   * Creates a new ItemBuilder with the given material.
   *
   * @param material The material of the item.
   * @return The new ItemBuilder.
   */
  @Contract("_ -> new")
  public static @NotNull ItemBuilder of(@NotNull Material material) {
    return new ItemBuilder(material);
  }

  /**
   * Creates a new ItemBuilder with the given material and display name.
   *
   * @param material  The material of the item.
   * @param component The display name of the item.
   * @return The new ItemBuilder.
   */
  public static @NotNull ItemBuilder of(@NotNull Material material, @NotNull Component component) {
    return new ItemBuilder(material, component);
  }

  /**
   * Creates a new ItemBuilder with the given item stack.
   *
   * @param itemStack The item stack.
   * @return The new ItemBuilder.
   */
  @Contract("_ -> new")
  public static @NotNull ItemBuilder of(@NotNull ItemStack itemStack) {
    return new ItemBuilder(itemStack);
  }

  /**
   * Gives the item unbreakable.
   *
   * @return The ItemBuilder.
   */
  public ItemBuilder unbreakable() {
    this.unbreakable = true;
    return this;
  }

  /**
   * Sets the material of the item.
   *
   * @param material The material.
   * @return The ItemBuilder.
   */
  public ItemBuilder material(@NotNull Material material) {
    this.material = material;
    return this;
  }

  /**
   * Sets the url of the skull.
   *
   * @param url The url of the skull.
   * @return The ItemBuilder.
   */
  public ItemBuilder skullUrl(@Nullable String url) {
    if (url == null) {
      return this;
    }

    this.skullUrl = url;
    return this;
  }

  /**
   * Sets the custom model data of the item.
   *
   * @param data The custom model data.
   * @return The ItemBuilder.
   */
  public ItemBuilder customModelData(@Nonnegative int data) {
    this.customModelData = data;
    return this;
  }

  /**
   * Sets the amount of the item.
   *
   * @param number The amount.
   * @return The ItemBuilder.
   */
  public ItemBuilder amount(@NotNull Number number) {
    int newAmount = number.intValue();
    if (newAmount > this.material.getMaxStackSize() || newAmount > 64) {
      newAmount = this.material.getMaxStackSize();
    }
    if (newAmount < 0) {
      newAmount = 1;
    }

    this.amount = newAmount;
    return this;
  }

  /**
   * Sets the display name of the item.
   *
   * @param displayName The display name.
   * @return The ItemBuilder.
   */
  public ItemBuilder displayName(@NotNull Component displayName) {
    this.displayName = displayName.decoration(TextDecoration.ITALIC, false);
    return this;
  }

  /**
   * Sets the display name of the item.
   *
   * @param displayName The display name.
   * @return The ItemBuilder.
   */
  public ItemBuilder displayName(@NotNull String displayName) {
    this.displayNameOld = displayName;
    return this;
  }

  /**
   * Appends the display name of the item.
   *
   * @param displayName The display name.
   * @return The ItemBuilder.
   */
  public ItemBuilder appendDisplayName(@NotNull Component displayName) {
    if (this.displayName == null) {
      this.displayName = Component.empty();
    }

    this.displayName = this.displayName.append(displayName.decoration(TextDecoration.ITALIC, false));
    return this;
  }

  /**
   * Appends the display name of the item.
   *
   * @param displayName The display name.
   * @return The ItemBuilder.
   */
  public ItemBuilder appendDisplayNameOld(@NotNull String displayName) {
    if (this.displayNameOld == null) {
      this.displayNameOld = "";
    }

    this.displayNameOld += displayName;
    return this;
  }

  /**
   * Appends the lore of the item.
   *
   * @param lore The lore.
   * @return The ItemBuilder.
   */
  public ItemBuilder appendLoreOld(@NotNull List<String> lore) {
    this.loreOld.addAll(lore);
    return this;
  }

  /**
   * Appends the lore of the item.
   *
   * @param lore The lore.
   * @return The ItemBuilder.
   */
  public ItemBuilder appendLoreOld(@NotNull String... lore) {
    this.loreOld.addAll(Arrays.asList(lore));
    return this;
  }

  /**
   * Add an item flag to the item.
   *
   * @param itemFlag The item flag.
   * @return The ItemBuilder.
   */
  public ItemBuilder itemFlag(@NotNull ItemFlag itemFlag) {
    this.flags.add(itemFlag);
    return this;
  }

  /**
   * Add item flags to the item.
   *
   * @param itemFlag The item flags.
   * @return The ItemBuilder.
   */
  public ItemBuilder itemFlags(ItemFlag @NotNull ... itemFlag) {
    this.flags.addAll(Arrays.asList(itemFlag));
    return this;
  }

  /**
   * Add all item flags to the item.
   *
   * @return The ItemBuilder.
   */
  public ItemBuilder allItemFlags() {
    this.flags.addAll(Arrays.asList(ItemFlag.values()));
    return this;
  }

  /**
   * Add an enchantment to the item.
   *
   * @param enchantment The enchantment.
   * @param value       The level of the enchantment.
   * @return The ItemBuilder.
   */
  public ItemBuilder enchantment(@NotNull Enchantment enchantment, @Nonnegative int value) {
    this.enchantments.put(enchantment, value);
    return this;
  }

  /**
   * Add a single line of lore to the item.
   *
   * @param line The line of lore.
   * @return The ItemBuilder.
   */
  public ItemBuilder lore(String line) {
    this.loreOld.add(line);
    return this;
  }

  /**
   * Add multiple lines of lore to the item.
   *
   * @param lines The lines of lore.
   * @return The ItemBuilder.
   */
  public ItemBuilder lore(Component @NotNull ... lines) {
    for (int i = 0; i < lines.length; i++) {
      lines[i] = lines[i].decoration(TextDecoration.ITALIC, false);
    }

    this.lore.addAll(Arrays.asList(lines));
    return this;
  }

  /**
   * Add multiple lines of lore to the item.
   *
   * @param lines The lines of lore.
   * @return The ItemBuilder.
   */
  public ItemBuilder lore(String @NotNull ... lines) {
    this.loreOld.addAll(Arrays.asList(lines));
    return this;
  }

  /**
   * Add multiple lines of lore to the item.
   *
   * @param list The lines of lore.
   * @return The ItemBuilder.
   */
  public ItemBuilder lore(@Nullable List<Component> list) {
    if (list == null) {
      return this;
    }

    try {
      list.replaceAll(component -> component.decoration(TextDecoration.ITALIC, false));
    } catch (UnsupportedOperationException e) {
      list = new ArrayList<>(list);
      list.replaceAll(component -> component.decoration(TextDecoration.ITALIC, false));
    }
    this.lore.addAll(list);
    return this;
  }

  /**
   * Add multiple lines of lore to the item.
   *
   * @param list The lines of lore.
   * @return The ItemBuilder.
   */
  public ItemBuilder loreOld(@Nullable List<String> list) {
    if (list == null) {
      return this;
    }

    this.loreOld.addAll(list);
    return this;
  }

  /**
   * Add multiple lines of lore to the item.
   *
   * @param items The lines of lore.
   * @return The ItemBuilder.
   */
  public ItemBuilder loreOld(@NotNull String... items) {
    this.loreOld.addAll(Arrays.stream(items).toList());
    return this;
  }

  /**
   * Add skull owner to the item.
   *
   * @param string The skull owner.
   * @return The ItemBuilder.
   */
  public ItemBuilder skullOwner(@NotNull String string) {
    this.skullOwner = Bukkit.getOfflinePlayer(string);
    return this;
  }

  /**
   * Add skull owner to the item.
   *
   * @param uuid The skull owner.
   * @return The ItemBuilder.
   */
  public ItemBuilder skullOwner(@NotNull UUID uuid) {
    this.skullOwner = Bukkit.getOfflinePlayer(uuid);
    return this;
  }

  /**
   * Add data to the item.
   *
   * @param key   The key of the data
   * @param type  The type of the data
   * @param value The value of the data
   * @param <T>   The type of the data
   * @param <Z>   The type of the data
   * @return The ItemBuilder.
   */
  public <T, Z> ItemBuilder dataContainer(@NotNull NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
    this.containers.add(new Container<>(key, value, type));
    return this;
  }

  /**
   * Builds the item.
   *
   * @return The item.
   */
  public @NotNull ItemStack build() {
    if (this.itemStack == null) {
      this.itemStack = new ItemStack(this.material, this.amount);
    }

    if (this.itemStack.getType() != this.material) {
      this.itemStack.setType(this.material);
    }

    if (this.itemMeta == null) {
      this.itemMeta = this.itemStack.getItemMeta();
    }

    if (this.itemMeta == null) {
      this.itemMeta = Bukkit.getItemFactory().getItemMeta(this.material);
    }

    this.itemStack.setAmount(this.amount);

    if (this.itemMeta == null) {
      return this.itemStack;
    }

    this.itemMeta.setUnbreakable(this.unbreakable);

    if (this.customModelData != -1) {
      this.itemMeta.setCustomModelData(this.customModelData);
    }

    if (this.displayNameOld != null) {
      this.displayName = PotPlugin.getLegacyComponentSerializer().deserialize(this.displayNameOld).decoration(TextDecoration.ITALIC, this.displayNameOld.contains("§o"));
    }

    this.itemMeta.displayName(this.displayName);

    if (!this.loreOld.isEmpty()) {
      for (String s : this.loreOld) {
        this.lore.add(PotPlugin.getLegacyComponentSerializer().deserialize(s).decoration(TextDecoration.ITALIC, s.contains("§o")));
      }
    }

    if (!this.lore.isEmpty()) {
      this.itemMeta.lore(this.lore);
    }

    if (!this.flags.isEmpty())
      this.flags.forEach(itemFlag -> this.itemMeta.addItemFlags(itemFlag));

    if (!this.enchantments.isEmpty()) {
      this.enchantments.forEach((enchantment, level) -> this.itemMeta.addEnchant(enchantment, level, true));
    }

    if (this.skullOwner != null && this.itemMeta instanceof SkullMeta skullMeta) {
      this.itemStack.setType(Material.PLAYER_HEAD);
      skullMeta.setOwningPlayer(this.skullOwner);
      this.itemMeta = skullMeta;
    }

    if (!this.containers.isEmpty() && this.itemMeta != null) {
      for (Container<?> container : this.containers) {
        this.itemMeta.getPersistentDataContainer().set(container.key(), container.type(), container.value());
      }
    }


    if (this.skullUrl != null && this.itemMeta instanceof SkullMeta skullMeta) {
      try {
        this.itemStack.setType(Material.PLAYER_HEAD);
        PlayerProfile profile = Bukkit.createProfile(PROFILE_UUID);
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
          urlObject = new URL(this.skullUrl);
        } catch (MalformedURLException exception) {
          Bukkit.getLogger().log(Level.SEVERE, "Invalid URL", exception);
          this.itemStack.setItemMeta(this.itemMeta);
          return this.itemStack;
        }
        textures.setSkin(urlObject);
        profile.setTextures(textures);
        skullMeta.setPlayerProfile(profile);

        this.itemMeta = skullMeta;
      } catch (Exception e) {
        System.out.println("Error while setting skull url: " + e.getMessage());
      }
    }

    this.itemStack.setItemMeta(this.itemMeta);
    return this.itemStack;
  }

  private record Container<Z>(NamespacedKey key, Z value, @SuppressWarnings("rawtypes") PersistentDataType type) {
  }
}
