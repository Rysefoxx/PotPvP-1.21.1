/*
 *      Copyright (c) 2023 Rysefoxx
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package io.potpvp.minecraft.command.bridge;

import javax.annotation.Nonnegative;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents the arguments of a command.
 *
 * @author Rysefoxx
 * @since 30.10.2023
 */
@Getter
@RequiredArgsConstructor
public class CommandArguments {

  private final String[] args;

  /**
   * Get the length of the arguments.
   *
   * @return the length of the arguments
   */
  public @Nonnegative int size() {
    return args.length;
  }

  /**
   * Get the argument at the index.
   *
   * @param index the index of the argument
   * @return the argument at the index
   */
  public @NotNull String element(@Nonnegative int index) {
    if (index >= args.length) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + args.length);
    }

    return args[index];
  }

  /**
   * Compares the argument at the index with the given string.
   *
   * @param index     the index of the argument
   * @param toCompare the string to compare
   * @return true if the argument at the index equals the given string
   */
  public boolean equalsIgnoreCase(@Nonnegative int index, @NotNull String toCompare) {
    return element(index).equalsIgnoreCase(toCompare);
  }

  /**
   * Compares the argument at the index with the given string. Atlas one of the strings must match.
   *
   * @param index     the index of the argument
   * @param toCompare the strings to compare
   * @return true if the argument at the index equals the given string
   */
  public boolean equalsIgnoreCase(@Nonnegative int index, @NotNull String... toCompare) {
    for (String string : toCompare) {
      if (element(index).equalsIgnoreCase(string)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if args has the length.
   *
   * @param length the length of the arguments
   * @return if the length of the arguments is equal to the length
   */
  public boolean isLength(@Nonnegative int length) {
    return args.length == length;
  }

  /**
   * Checks if greater than the length.
   *
   * @param length the length of the arguments
   * @return if the length of the arguments is greater than the length
   */
  public boolean greater(@Nonnegative int length) {
    return args.length > length;
  }

  /**
   * Checks if greater than or equal to the length.
   *
   * @param length the length of the arguments
   * @return if the length of the arguments is greater than or equal to the length
   */
  public boolean greaterEqual(@Nonnegative int length) {
    return args.length >= length;
  }

  /**
   * Checks if smaller than the length.
   *
   * @param length the length of the arguments
   * @return if the length of the arguments is less than the length
   */
  public boolean smaller(@Nonnegative int length) {
    return args.length < length;
  }

  /**
   * Checks if smaller than or equal to the length.
   *
   * @param length the length of the arguments
   * @return if the length of the arguments is less than or equal to the length
   */
  public boolean smallerEqual(@Nonnegative int length) {
    return args.length <= length;
  }

  /**
   * Get the length of the arguments.
   *
   * @return the length of the arguments
   */
  public @Nonnegative int length() {
    return args.length;
  }

  /**
   * join the arguments with a space.
   *
   * @return the arguments as a string
   */
  public @NotNull String join() {
    return join(" ");
  }

  /**
   * join the arguments with a space and start at the offset.
   *
   * @param offset The offset to start at.
   * @return the arguments as a string
   */
  public @NotNull String join(@Nonnegative int offset) {
    return join(" ", offset);
  }

  /**
   * join with the given delimiter.
   *
   * @param delimiter the delimiter
   * @return the arguments as a string
   */
  public @NotNull String join(@NotNull String delimiter) {
    return String.join(delimiter, args);
  }

  /**
   * join with the given delimiter and start at the offset.
   *
   * @param delimiter the delimiter
   * @param offset    The offset to start at.
   * @return the arguments as a string
   */
  public @NotNull String join(@NotNull String delimiter, @Nonnegative int offset) {
    String[] split = new String[args.length - offset];
    System.arraycopy(args, offset, split, 0, split.length);
    return String.join(delimiter, split);
  }
}
