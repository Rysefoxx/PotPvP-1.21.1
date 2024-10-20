package PotPvP.extensions.java.lang.String;

import io.potpvp.minecraft.PotPlugin;
import io.potpvp.minecraft.service.language.LanguageService;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.ParsingException;
import org.jetbrains.annotations.NotNull;

/**
 * Extension methods for the {@link String} class.
 *
 * @author Rysefoxx
 * @since 06.10.2024
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Extension
public class StringExtension {

  private static final LanguageService LANGUAGE_SERVICE = PotPlugin.getContext().getBean(LanguageService.class);

  /**
   * Checks if the given string is null or whitespace.
   *
   * @param thiz The string to check.
   * @return True if the string is null or whitespace, false otherwise.
   */
  public static boolean isNullOrWhitespace(@This String thiz) {
    return thiz == null || thiz.trim().isEmpty();
  }

  /**
   * Translates the given string with the given UUID.
   *
   * @param messageKey The message key to translate.
   * @param uuid       The UUID of the player
   * @return The translated string.
   */
  public static @NotNull String translate(@This String messageKey, @NotNull UUID uuid, Object... args) {
    String message = LANGUAGE_SERVICE.getMessage(uuid, messageKey);
    if (args.length == 0) {
      return message;
    }

    return interpolate(message, args);
  }

  /**
   * Interpolates the given string with the given arguments.
   *
   * @param template The template string.
   * @param args     The arguments to interpolate.
   * @return The interpolated string.
   */
  public static String interpolate(@This String template, Object... args) {
    String result = template;
    for (int i = 0; i < args.length; i++) {
      result = result.replace(STR."{\{i}" + "}", args[i].toString());
    }
    return result;
  }

  /**
   * Deserializes the given string to a {@link Component}.
   *
   * @param message The string to deserialize.
   * @return The deserialized component.
   */
  public static Optional<Component> toComponent(@This String message) {
    return toComponentOrFallback(message, Optional.empty());
  }
  /**
   * Deserializes the given string to a {@link Component}.
   *
   * @param message  The string to deserialize.
   * @param fallback The component to return if the deserialization fails. Could be empty
   * @return The deserialized component.
   */
  public static Optional<Component> toComponentOrFallback(@This String message, Optional<Component> fallback) {
    try {
      //Support for main formatting -> <gradient:#fff> | <#fff> | <red>
      return Optional.of(PotPlugin.getMiniMessage().deserialize(message));
    } catch (ParsingException exception) {
      //Support for old school formatting -> §f
      Component component = PotPlugin.getLegacyComponentSerializer().deserialize(message);
      return Optional.of(component);
    } catch (Exception exception) {
      //fallback
      return fallback;
    }
  }
}