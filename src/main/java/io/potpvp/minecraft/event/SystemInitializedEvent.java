package io.potpvp.minecraft.event;

import io.potpvp.minecraft.PotPlugin;
import lombok.RequiredArgsConstructor;

/**
 * This event is called when the system is initialized.
 *
 * @author Rysefoxx
 * @since 13.10.2024
 */
@RequiredArgsConstructor
public class SystemInitializedEvent {

  private final PotPlugin plugin;
}