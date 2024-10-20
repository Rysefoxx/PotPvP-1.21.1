package io.potpvp.minecraft.repository.player;

import io.potpvp.minecraft.entity.player.PlayerEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository manages the player entries.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, UUID> {
}
