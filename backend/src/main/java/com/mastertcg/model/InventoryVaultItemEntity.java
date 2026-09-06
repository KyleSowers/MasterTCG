package com.mastertcg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory_vault_items")
public class InventoryVaultItemEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_variant_id", nullable = false)
    private CardVariantEntity cardVariant;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_condition", nullable = false)
    private InventoryCardCondition cardCondition = InventoryCardCondition.UNKNOWN;

    @Column(name = "available_for_trade", nullable = false)
    private boolean availableForTrade = false;

    @Column(name = "available_for_sale", nullable = false)
    private boolean availableForSale = false;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void beforeCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }

        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        updatedAt = now;
    }

    @PreUpdate
    public void beforeUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public CardVariantEntity getCardVariant() {
        return cardVariant;
    }

    public void setCardVariant(CardVariantEntity cardVariant) {
        this.cardVariant = cardVariant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

        public InventoryCardCondition getCardCondition() {
        return cardCondition;
    }

    public void setCardCondition(InventoryCardCondition cardCondition) {
        this.cardCondition = cardCondition;
    }

    public boolean isAvailableForTrade() {
        return availableForTrade;
    }

    public void setAvailableForTrade(boolean availableForTrade) {
        this.availableForTrade = availableForTrade;
    }

    public boolean isAvailableForSale() {
        return availableForSale;
    }

    public void setAvailableForSale(boolean availableForSale) {
        this.availableForSale = availableForSale;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


}
