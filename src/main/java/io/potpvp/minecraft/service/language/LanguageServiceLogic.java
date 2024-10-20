package io.potpvp.minecraft.service.language;

import io.potpvp.minecraft.entity.language.LanguageEntity;
import io.potpvp.minecraft.service.EntityService;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * This service manages the player entries.
 *
 * @author Rysefoxx
 * @since 20.10.2024
 */
public interface LanguageServiceLogic extends EntityService<LanguageEntity> {

  /**
   * Fetches the message for the given {@link UUID}.
   *
   * @param uuid       to get the player selected language.
   * @param messageKey the key to get the message for
   * @return the message for the given {@link UUID}
   */
  @NotNull
  String getMessage(@NotNull UUID uuid, @NotNull String messageKey);

  /**
   * Creates or fetches the language entity for the given {@link UUID}.
   *
   * @param uuid the {@link UUID} to get the language entity for
   * @return the language entity for the given {@link UUID}
   */
  @NotNull
  LanguageEntity getOrCreate(@NotNull UUID uuid);

}
