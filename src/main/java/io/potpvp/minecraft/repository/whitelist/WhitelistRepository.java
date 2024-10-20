package io.potpvp.minecraft.repository.whitelist;

import io.potpvp.minecraft.entity.whitelist.WhitelistEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository is used to manage the {@link WhitelistEntity}.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@Repository
public interface WhitelistRepository extends JpaRepository<WhitelistEntity, UUID> {
}