package com.mastertcg.inventory;

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

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
