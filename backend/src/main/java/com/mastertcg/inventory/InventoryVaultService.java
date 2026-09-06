package com.mastertcg.inventory;

import com.mastertcg.model.CardEntity;
import com.mastertcg.model.CardVariantEntity;
import com.mastertcg.model.InventoryVaultItemEntity;
import com.mastertcg.repository.CardVariantRepository;
import com.mastertcg.repository.InventoryVaultItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryVaultService {

    // ---------------------------------------------------------------------------
    // TEMPORARY DEMO USER
    // ---------------------------------------------------------------------------

    private static final UUID DEMO_USER_ID =
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    private final InventoryVaultItemRepository inventoryVaultItemRepository;
    private final CardVariantRepository cardVariantRepository;

    public InventoryVaultService(
            InventoryVaultItemRepository inventoryVaultItemRepository,
            CardVariantRepository cardVariantRepository
    ) {
        this.inventoryVaultItemRepository = inventoryVaultItemRepository;
        this.cardVariantRepository = cardVariantRepository;
    }


    // ---------------------------------------------------------------------------
    // DEMO INVENTORY VAULT API METHODS
    // ---------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<InventoryVaultItemResponse> getDemoVaultItems() {
        return inventoryVaultItemRepository.findByUserId(DEMO_USER_ID)
                .stream()
                .sorted(Comparator.comparing(item ->
                        item.getCardVariant().getCard().getCardNumber()
                ))
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public Optional<InventoryVaultItemResponse> setDemoVaultItemQuantity(
            InventoryVaultItemRequest request
    ) {
        if (request.quantity() <= 0) {
            inventoryVaultItemRepository
                    .findByUserIdAndCardVariant_Id(DEMO_USER_ID, request.cardVariantId())
                    .ifPresent(inventoryVaultItemRepository::delete);

            return Optional.empty();
        }

        CardVariantEntity cardVariant = cardVariantRepository
                .findById(request.cardVariantId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Card variant not found: " + request.cardVariantId()
                ));

        InventoryVaultItemEntity vaultItem = inventoryVaultItemRepository
                .findByUserIdAndCardVariant_Id(DEMO_USER_ID, request.cardVariantId())
                .orElseGet(() -> {
                    InventoryVaultItemEntity newItem = new InventoryVaultItemEntity();
                    newItem.setUserId(DEMO_USER_ID);
                    newItem.setCardVariant(cardVariant);
                    return newItem;
                });

        vaultItem.setQuantity(request.quantity());

        if (request.cardCondition() != null) {
            vaultItem.setCardCondition(request.cardCondition());
        }

        if (request.availableForTrade() != null) {
            vaultItem.setAvailableForTrade(request.availableForTrade());
        }

        if (request.availableForSale() != null) {
            vaultItem.setAvailableForSale(request.availableForSale());
        }

        if (request.notes() != null) {
            String trimmedNotes = request.notes().trim();
            vaultItem.setNotes(trimmedNotes.isEmpty() ? null : trimmedNotes);
        }

        InventoryVaultItemEntity savedItem = inventoryVaultItemRepository.save(vaultItem);

        return Optional.of(toResponse(savedItem));
    }


    // ---------------------------------------------------------------------------
    // MAPPING HELPERS
    // ---------------------------------------------------------------------------

    private InventoryVaultItemResponse toResponse(InventoryVaultItemEntity vaultItem) {
        CardVariantEntity cardVariant = vaultItem.getCardVariant();
        CardEntity card = cardVariant.getCard();

        return new InventoryVaultItemResponse(
                vaultItem.getId(),
                vaultItem.getUserId(),

                cardVariant.getId(),
                card.getId(),
                card.getSet().getId(),

                card.getCardNumber(),
                card.getName(),
                cardVariant.getFinish().name(),
                card.getRarity(),
                card.getImageSmallUrl(),

                vaultItem.getQuantity(),
                vaultItem.getCardCondition(),
                vaultItem.isAvailableForTrade(),
                vaultItem.isAvailableForSale(),
                vaultItem.getNotes(),

                vaultItem.getCreatedAt(),
                vaultItem.getUpdatedAt()
        );
    }
}
