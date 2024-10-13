package io.potpvp.minecraft.repository.whitelist;

import io.potpvp.minecraft.entity.whitelist.WhitelistEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository saves a whitelist entity.
 *
 * @author Rysefoxx
 * @since 06.10.2024
 */
@Repository
public interface WhitelistRepository extends JpaRepository<WhitelistEntity, UUID> {

}
