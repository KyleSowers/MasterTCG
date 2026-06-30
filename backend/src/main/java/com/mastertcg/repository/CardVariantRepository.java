package com.mastertcg.repository;

import com.mastertcg.model.CardVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface CardVariantRepository extends JpaRepository<CardVariantEntity, UUID> {
    List<CardVariantEntity> findByCard_Set_Id(UUID setId);
}
