package com.mastertcg.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "card_variants")
public class CardVariantEntity {

    @Id
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    private CardEntity card;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardFinish finish;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public CardEntity getCard() { return card; }
    public void setCard(CardEntity card) { this.card = card; }

    public CardFinish getFinish() { return finish; }
    public void setFinish(CardFinish finish) { this.finish = finish; }
}
