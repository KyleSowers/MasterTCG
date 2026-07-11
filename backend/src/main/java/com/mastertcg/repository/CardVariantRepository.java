package com.mastertcg.repository;

import com.mastertcg.model.CardVariantEntity;
import com.mastertcg.model.CardFinish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface CardVariantRepository extends JpaRepository<CardVariantEntity, UUID> {
    List<CardVariantEntity> findByCard_Set_Id(UUID setId);

    Optional<CardVariantEntity> findByCard_IdAndFinish(
        UUID cardId,
        CardFinish finish
    );
}
