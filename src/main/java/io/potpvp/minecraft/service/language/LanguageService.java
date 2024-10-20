package io.potpvp.minecraft.service.language;

import io.potpvp.minecraft.database.ResultHandler;
import io.potpvp.minecraft.entity.language.LanguageEntity;
import io.potpvp.minecraft.event.SystemInitializedEvent;
import io.potpvp.minecraft.factory.EntityProxyFactory;
import io.potpvp.minecraft.language.Language;
import io.potpvp.minecraft.repository.language.LanguageRepository;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for handling language translations.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LanguageService implements LanguageServiceLogic {

  private final LanguageRepository repository;
  private final Set<LanguageEntity> cache = ConcurrentHashMap.newKeySet();

  private final Map<Locale, Map<String, String>> translations = new HashMap<>();

  @Override
  public void delete(@NotNull LanguageEntity toDelete, @NotNull ResultHandler<LanguageEntity> resultHandler) {
    CompletableFuture.runAsync(() -> {
      try {
        this.repository.deleteById(toDelete.getUuid());
        this.cache.remove(toDelete);
        resultHandler.success(toDelete);
      } catch (Exception exception) {
        resultHandler.error(toDelete, exception);
      }
    });
  }

  @Override
  public void load(@NotNull SystemInitializedEvent event) {
    this.repository.findAll().forEach(entity -> this.cache.add(EntityProxyFactory.createProxy(entity, this.repository)));
    for (Language language : Language.values()) {
      ResourceBundle resourceBundle = ResourceBundle.getBundle("message", language.getLocale());

      Map<String, String> currentTranslations = this.translations.computeIfAbsent(language.getLocale(), _ -> new HashMap<>());
      resourceBundle.getKeys().asIterator().forEachRemaining(key -> currentTranslations.put(key, resourceBundle.getString(key)));
      this.translations.put(language.getLocale(), currentTranslations);
    }
  }

  @Override
  public @NotNull String getMessage(@NotNull UUID uuid, @NotNull String messageKey) {
    LanguageEntity entity = getOrCreate(uuid);
    Map<String, String> currentTranslations = this.translations.get(entity.getLanguage().getLocale());
    return currentTranslations.getOrDefault(messageKey, messageKey);
  }

  @Override
  public @NotNull LanguageEntity getOrCreate(@NotNull UUID uuid) {
    Optional<LanguageEntity> optional = this.cache.stream()
        .filter(entity -> entity.getUuid().equals(uuid))
        .findFirst();

    if (optional.isEmpty()) {
      LanguageEntity entity = EntityProxyFactory.createProxy(LanguageEntity.class, this.repository);
      entity.setUuid(uuid);
      entity.setLanguage(Language.GERMAN);

      this.cache.add(entity);
      return entity;
    }

    return optional.get();
  }
}