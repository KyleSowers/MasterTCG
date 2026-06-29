package com.mastertcg.repository;

import com.mastertcg.model.UserCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserCardRepository extends JpaRepository<UserCardEntity, UUID> {

    List<UserCardEntity> findByUserId(UUID userId);

    Optional<UserCardEntity> findByUserIdAndCard_Id(UUID userId, UUID cardId);
    
}
