package com.mastertcg.repository;

import com.mastertcg.model.InventoryVaultItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryVaultItemRepository extends JpaRepository<InventoryVaultItemEntity, UUID> {

    List<InventoryVaultItemEntity> findByUserId(UUID userId);

    Optional<InventoryVaultItemEntity> findByUserIdAndCardVariant_Id(
            UUID userId,
            UUID cardVariantId
    );
}
