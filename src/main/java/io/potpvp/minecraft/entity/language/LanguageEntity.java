package io.potpvp.minecraft.entity.language;

import io.potpvp.minecraft.annotation.AutoSave;
import io.potpvp.minecraft.language.Language;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * This entity represents a language entry.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@Entity
@Setter(onMethod_ = @AutoSave)
@Getter
public class LanguageEntity {

  @Id
  private UUID uuid;
  @Enumerated(EnumType.STRING)
  private Language language;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LanguageEntity that = (LanguageEntity) o;
    return Objects.equals(uuid, that.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(uuid);
  }

  @Override
  public String toString() {
    return "LanguageEntity{" +
        "uuid=" + uuid +
        ", language=" + language +
        '}';
  }
}