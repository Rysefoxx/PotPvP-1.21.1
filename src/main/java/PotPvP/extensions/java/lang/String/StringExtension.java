package PotPvP.extensions.java.lang.String;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

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

  /**
   * Checks if the given string is null or whitespace.
   *
   * @param thiz The string to check.
   * @return True if the string is null or whitespace, false otherwise.
   */
  public static boolean isNullOrWhitespace(@This String thiz) {
    return thiz == null || thiz.trim().isEmpty();
  }
}