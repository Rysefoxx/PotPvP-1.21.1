package io.potpvp.minecraft.core;

import static io.potpvp.minecraft.core.CoreConstants.Command.USAGE;
import static io.potpvp.minecraft.core.CoreConstants.General.PERMISSION_PREFIX;
import static io.potpvp.minecraft.core.CoreConstants.General.PREFIX;

import io.potpvp.minecraft.PotPlugin;
import io.potpvp.minecraft.service.language.LanguageService;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a player with additional methods.
 *
 * @author Rysefoxx
 * @since 14.10.2024
 */
@Getter
public class CorePlayer {

  private static final String OFFLINE = "Player with UUID {0} is not online.";

  private final PotPlugin plugin;
  private final UUID uuid;

  private final LanguageService languageService;

  /**
   * Creates a new {@link CorePlayer} with the given {@link UUID} and {@link PotPlugin} plugin.
   *
   * @param uuid   The {@link UUID} of the player
   * @param plugin The {@link PotPlugin} plugin
   */
  public CorePlayer(@NotNull UUID uuid, @NotNull PotPlugin plugin) {
    this.plugin = plugin;
    this.uuid = uuid;
    this.languageService = PotPlugin.getContext().getBean(LanguageService.class);
  }

  /**
   * Fetches the player.
   *
   * @return The player
   */
  public Player getPlayer() {
    return Bukkit.getPlayer(this.uuid);
  }

  /**
   * Checks if the player has the given permission.
   *
   * @param permission The permission to check
   * @return {@code true} if the player has the permission, otherwise {@code false}
   */
  public boolean hasPermission(String permission) {
    Player player = getPlayer();
    if (player == null) {
      PotPlugin.getLog().warningExt(OFFLINE.interpolate(this.uuid));
      return false;
    }

    return player.hasPermission(PERMISSION_PREFIX + permission);
  }

  /**
   * Checks if the player is online.
   *
   * @return {@code true} if the player is online, otherwise {@code false}
   */
  public boolean isOnline() {
    return getPlayer() != null;
  }

  /**
   * Sends a translated message to the player.
   *
   * @param messageKey The messageKey to find the translation for
   */
  public void translate(@NotNull String messageKey) {
    translate(messageKey, PREFIX);
  }

  /**
   * Sends a translated message to the player.
   *
   * @param messageKey   The messageKey to find the translation for
   * @param replacements The replacements to replace in the message
   */
  public void translate(@NotNull String messageKey, Object... replacements) {
    translate(messageKey, PREFIX, replacements);
  }

  /**
   * Sends a translated message to the player.
   *
   * @param messageKey The messageKey to find the translation for
   * @param prefix     The prefix to use
   */
  public void translate(@NotNull String messageKey, @NotNull String prefix) {
    translate(messageKey, prefix, new Object[0]);
  }

  /**
   * Sends a translated message to the player.
   *
   * @param messageKey   The messageKey to find the translation for
   * @param prefix       The prefix to use
   * @param replacements The replacements to replace in the message
   */
  public void translate(@NotNull String messageKey, @NotNull String prefix, Object... replacements) {
    Player player = getPlayer();
    if (player == null) {
      PotPlugin.getLog().warningExt(OFFLINE.interpolate(this.uuid));
      return;
    }

    String message;
    if (replacements.length == 0) {
      message = this.languageService.getMessage(this.uuid, messageKey);
    } else {
      message = this.languageService.getMessage(this.uuid, messageKey).interpolate(replacements);
    }

    Optional<Component> parsedMessage = message.toComponent();
    Optional<Component> parsedPrefix = prefix.toComponent();

    if (parsedMessage.isEmpty() || parsedPrefix.isEmpty()) {
      return;
    }

    player.sendMessage(parsedPrefix.get().append(parsedMessage.get()));
  }

  /**
   * Send a help message to the player.
   *
   * @param helps The help messages to send.
   */
  public void sendHelp(String @NotNull ... helps) {
    Player player = getPlayer();
    if (player == null) {
      PotPlugin.getLog().warningExt(OFFLINE.interpolate(this.uuid));
      return;
    }

    Optional<Component> optionalTranslatedUsage = this.languageService.getMessage(this.uuid, USAGE).toComponent();
    if (optionalTranslatedUsage.isEmpty()) {
      PotPlugin.getLog().warningExt("An error occurred while parsing the usage message.");
      return;
    }

    for (String help : helps) {
      Optional<Component> optionalTranslatedHelp = help.toComponent();
      if (optionalTranslatedHelp.isEmpty()) {
        PotPlugin.getLog().warningExt("An error occurred while parsing the help message.");
        continue;
      }

      player.sendMessage(optionalTranslatedUsage.get().append(optionalTranslatedHelp.get()));
    }
  }

  /**
   * It sends an action bar to the player.
   *
   * @param message The message to send to the player.
   */
  public void info(@Nullable String message) {
    Player player = getPlayer();
    if (player == null) {
      PotPlugin.getLog().warningExt(OFFLINE.interpolate(this.uuid));
      return;
    }

    if (message == null) {
      return;
    }

    message.toComponent().ifPresent(player::sendActionBar);
  }


  /**
   * Plays default ui click sound.
   */
  public void soundUiClick() {
    Player player = getPlayer();
    if (player == null) {
      PotPlugin.getLog().warningExt(OFFLINE.interpolate(this.uuid));
      return;
    }

    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
  }

  /**
   * Send a {@link Sound} to the player.
   *
   * @param sound The sound to send
   */
  public void sound(@NotNull Sound sound) {
    Player player = getPlayer();
    if (player == null) {
      PotPlugin.getLog().warningExt(OFFLINE.interpolate(this.uuid));
      return;
    }

    player.playSound(player.getLocation(), sound, 1, 1);
  }

  /**
   * Close the inventory of the player.
   */
  public void closeInventory() {
    Player player = getPlayer();
    if (player == null) {
      PotPlugin.getLog().warningExt(OFFLINE.interpolate(this.uuid));
      return;
    }

    player.closeInventory();
  }
}