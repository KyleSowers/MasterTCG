package com.mastertcg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "collection_profiles")
public class CollectionProfileEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "collection_style", nullable = false, length = 40)
    private String collectionStyle;

    @Column(name = "include_normal", nullable = false)
    private boolean includeNormal = true;

    @Column(name = "include_holo", nullable = false)
    private boolean includeHolo = true;

    @Column(name = "include_reverse_holo", nullable = false)
    private boolean includeReverseHolo = true;

    @Column(name = "include_special_finishes", nullable = false)
    private boolean includeSpecialFinishes = true;

    @Column(name = "include_common", nullable = false)
    private boolean includeCommon = true;

    @Column(name = "include_uncommon", nullable = false)
    private boolean includeUncommon = true;

    @Column(name = "include_rare", nullable = false)
    private boolean includeRare = true;

    @Column(name = "include_main_cards", nullable = false)
    private boolean includeMainCards = true;

    @Column(name = "include_secret_cards", nullable = false)
    private boolean includeSecretCards = true;

    // ---------------------------------------------------------------------------
    // SELECTED SETS
    // ---------------------------------------------------------------------------
    // Stores which card sets this profile tracks.
    // Backed by the collection_profile_sets join table.
    // ---------------------------------------------------------------------------

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "collection_profile_sets",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "set_id", nullable = false)
    private Set<UUID> selectedSetIds = new LinkedHashSet<>();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollectionStyle() {
        return collectionStyle;
    }

    public void setCollectionStyle(String collectionStyle) {
        this.collectionStyle = collectionStyle;
    }

    public boolean isIncludeNormal() {
        return includeNormal;
    }

    public void setIncludeNormal(boolean includeNormal) {
        this.includeNormal = includeNormal;
    }

    public boolean isIncludeHolo() {
        return includeHolo;
    }

    public void setIncludeHolo(boolean includeHolo) {
        this.includeHolo = includeHolo;
    }

    public boolean isIncludeReverseHolo() {
        return includeReverseHolo;
    }

    public void setIncludeReverseHolo(boolean includeReverseHolo) {
        this.includeReverseHolo = includeReverseHolo;
    }

    public boolean isIncludeSpecialFinishes() {
        return includeSpecialFinishes;
    }

    public void setIncludeSpecialFinishes(boolean includeSpecialFinishes) {
        this.includeSpecialFinishes = includeSpecialFinishes;
    }

    public boolean isIncludeCommon() {
        return includeCommon;
    }

    public void setIncludeCommon(boolean includeCommon) {
        this.includeCommon = includeCommon;
    }

    public boolean isIncludeUncommon() {
        return includeUncommon;
    }

    public void setIncludeUncommon(boolean includeUncommon) {
        this.includeUncommon = includeUncommon;
    }

    public boolean isIncludeRare() {
        return includeRare;
    }

    public void setIncludeRare(boolean includeRare) {
        this.includeRare = includeRare;
    }

    public boolean isIncludeMainCards() {
        return includeMainCards;
    }

    public void setIncludeMainCards(boolean includeMainCards) {
        this.includeMainCards = includeMainCards;
    }

    public boolean isIncludeSecretCards() {
        return includeSecretCards;
    }

    public void setIncludeSecretCards(boolean includeSecretCards) {
        this.includeSecretCards = includeSecretCards;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Set<UUID> getSelectedSetIds() {
        return selectedSetIds;
    }

    public void setSelectedSetIds(Set<UUID> selectedSetIds) {
        this.selectedSetIds = selectedSetIds;
    }
}
