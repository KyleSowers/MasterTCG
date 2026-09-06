package com.mastertcg.inventory;

import com.mastertcg.model.InventoryCardCondition;

import java.time.LocalDateTime;
import java.util.UUID;

public record InventoryVaultItemResponse(
        UUID id,
        UUID userId,
        UUID cardVariantId,
        UUID cardId,
        UUID setId,
        String cardNumber,
        String cardName,
        String finish,
        String rarity,
        String imageSmallUrl,
        int quantity,
        InventoryCardCondition cardCondition,
        boolean availableForTrade,
        boolean availableForSale,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
