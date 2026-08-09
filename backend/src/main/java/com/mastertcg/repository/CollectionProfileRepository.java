package com.mastertcg.repository;

import com.mastertcg.model.CollectionProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CollectionProfileRepository extends JpaRepository<CollectionProfileEntity, UUID> {

    List<CollectionProfileEntity> findByUserId(UUID userId);
}
