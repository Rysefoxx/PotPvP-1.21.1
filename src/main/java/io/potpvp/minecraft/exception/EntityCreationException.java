package io.potpvp.minecraft.exception;

/**
 * This exception is thrown when an entity creation fails.
 *
 * @author Rysefoxx
 * @since 13.10.2024
 */
public class EntityCreationException extends RuntimeException {

  public EntityCreationException(String message) {
    super(message);
  }

  public EntityCreationException(String message, Throwable cause) {
    super(message, cause);
  }
}
