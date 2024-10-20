package PotPvP.extensions.java.util.logging.Logger;

import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.jetbrains.annotations.NotNull;

/**
 * Extension methods for the {@link Logger} class.
 *
 * @author Rysefoxx
 * @since 14.10.2024
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Extension
public class LoggerExtension {

  /**
   * Logs a warning message with the given message.
   *
   * @param logger  The logger to log to.
   * @param message The message to log.
   */
  public static void warningExt(@This Logger logger, @NotNull String message) {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    String callingClassName = stackTrace[3].getClassName();
    String callingMethodName = stackTrace[3].getMethodName();
    logger.warning("[{0}->{1}] {2}".interpolate(callingClassName, callingMethodName, message));
  }

  /**
   * Logs a error message with the given throwable.
   *
   * @param logger    The logger to log to.
   * @param message   The message to log.
   * @param throwable The throwable to log.
   */
  public static void severeExt(@This Logger logger, @NotNull String message, @NotNull Throwable throwable) {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    String callingClassName = stackTrace[3].getClassName();
    String callingMethodName = stackTrace[3].getMethodName();
    logger.log(Level.SEVERE, "[{0}->{1}] {2}".interpolate(callingClassName, callingMethodName, message), throwable);
  }

}