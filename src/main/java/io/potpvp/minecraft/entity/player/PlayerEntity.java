package io.potpvp.minecraft.entity.player;

import io.potpvp.minecraft.annotation.AutoSave;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * This entity represents a player entry.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@Entity
@Setter(onMethod_ = @AutoSave)
@Getter
public class PlayerEntity {

  @Id
  private UUID uuid;
  private String playerName;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerEntity that = (PlayerEntity) o;
    return Objects.equals(uuid, that.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(uuid);
  }

  @Override
  public String toString() {
    return "PlayerEntity{" +
        "uuid=" + uuid +
        ", playerName='" + playerName + '\'' +
        '}';
  }
}