package io.potpvp.minecraft.entity.whitelist;

import io.potpvp.minecraft.annotation.AutoSave;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This entity represents a whitelist entry.
 *
 * @author Rysefoxx
 * @since 06.10.2024
 */
@Entity
@Setter(onMethod_ = @AutoSave)
@Getter
public class WhitelistEntity {

  @Id
  private UUID uuid;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WhitelistEntity that = (WhitelistEntity) o;
    return Objects.equals(uuid, that.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(uuid);
  }
}