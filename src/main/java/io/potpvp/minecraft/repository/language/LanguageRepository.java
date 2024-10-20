package io.potpvp.minecraft.repository.language;

import io.potpvp.minecraft.entity.language.LanguageEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository is used to manage the {@link LanguageEntity}.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity, UUID> {
}