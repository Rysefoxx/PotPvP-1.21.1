package io.potpvp.minecraft.service;

import io.potpvp.minecraft.database.ResultHandler;
import io.potpvp.minecraft.event.SystemInitializedEvent;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;

/**
 * This service handles entities. Currently deleting and loading entities is supported.
 *
 * @author Rysefoxx
 * @since 13.10.2024
 */
public interface EntityService<T> {

  void delete(@NotNull T toDelete, @NotNull ResultHandler<T> resultHandler);

  @EventListener(SystemInitializedEvent.class)
  default void load(@NotNull SystemInitializedEvent event) {
  }

}