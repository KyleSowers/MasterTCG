package com.mastertcg.inventory;

import com.mastertcg.model.InventoryCardCondition;

import java.util.UUID;

public record InventoryVaultItemRequest(
        UUID cardVariantId,
        int quantity,
        InventoryCardCondition cardCondition,
        Boolean availableForTrade,
        Boolean availableForSale,
        String notes
) {}
