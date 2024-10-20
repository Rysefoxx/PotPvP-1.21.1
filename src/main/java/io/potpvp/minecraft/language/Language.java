package io.potpvp.minecraft.language;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the language of the plugin.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@RequiredArgsConstructor
@Getter
public enum Language {

  ENGLISH(Locale.US),
  GERMAN(Locale.GERMANY);

  private final Locale locale;

  /**
   * Fetches the language by the given language.
   *
   * @param language The language to fetch
   * @return The language
   */
  public static Optional<Language> getLanguage(String language) {
    return values().stream()
        .filter(value -> value.name().equalsIgnoreCase(language))
        .findFirst();
  }

  /**
   * Gets all available languages.
   *
   * @return The available languages
   */
  public static @NotNull List<String> getLanguages() {
    return values().stream()
        .map(Enum::name)
        .toList();
  }
}
