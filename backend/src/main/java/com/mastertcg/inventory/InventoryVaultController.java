package com.mastertcg.inventory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryVaultController {

    private final InventoryVaultService inventoryVaultService;

    public InventoryVaultController(InventoryVaultService inventoryVaultService) {
        this.inventoryVaultService = inventoryVaultService;
    }


    // ---------------------------------------------------------------------------
    // DEMO INVENTORY VAULT ENDPOINTS
    // ---------------------------------------------------------------------------
    // These endpoints use a temporary demo user ID inside InventoryVaultService.
    //
    // Inventory Vault is intentionally separate from collection completion.
    // It tracks extra/loose physical cards outside a binder/profile goal.
    // ---------------------------------------------------------------------------

    @GetMapping("/demo")
    public List<InventoryVaultItemResponse> getDemoVaultItems() {
        return inventoryVaultService.getDemoVaultItems();
    }

    @PostMapping("/demo/items")
    public ResponseEntity<InventoryVaultItemResponse> setDemoVaultItemQuantity(
            @RequestBody InventoryVaultItemRequest request
    ) {
        return inventoryVaultService.setDemoVaultItemQuantity(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
