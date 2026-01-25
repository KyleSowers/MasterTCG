package com.mastertcg.repository;

import com.mastertcg.model.SetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SetRepository extends JpaRepository<SetEntity, UUID> {
}
