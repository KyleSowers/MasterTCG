package com.mastertcg.repository;

import com.mastertcg.model.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<CardEntity, UUID>{
    List<CardEntity> findBySet_IdOrderByCardNumberAsc(UUID setId);

    Optional<CardEntity> findBySet_IdAndCardNumber(UUID setId, String cardNumber);
}
