package com.mastertcg.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_cards")
public class UserCardEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    private CardEntity card;

    @Column(name = "owned_count", nullable = false)
    private int ownedCount;

    private String condition;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public CardEntity getCard() { return card; }
    public void setCard(CardEntity card) { this.card = card; }

    public int getOwnedCount() { return ownedCount; }
    public void setOwnedCount(int ownedCount) { this.ownedCount = ownedCount; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }


}
