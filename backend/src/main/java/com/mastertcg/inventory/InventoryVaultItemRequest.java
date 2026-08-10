package com.mastertcg.inventory;

import java.util.UUID;

public record InventoryVaultItemRequest(
        UUID cardVariantId,
        int quantity
) {
}
