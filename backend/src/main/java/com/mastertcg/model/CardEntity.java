package com.mastertcg.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "cards")
public class CardEntity {

    @Id
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "set_id", nullable = false)
    private SetEntity set;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String rarity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardFinish finish;

    @Column(name = "variant_group")
    private String variantGroup;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public SetEntity getSet() { return set; }
    public void setSet(SetEntity set) { this.set = set; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRarity() { return rarity; }
    public void setRarity(String rarity) { this.rarity = rarity; }

    public CardFinish getFinish() { return finish; }
    public void setFinish(CardFinish finish) { this.finish = finish;}

    public String getVariantGroup() { return variantGroup; }
    public void setVariantGroup(String variantGroup) { this.variantGroup = variantGroup; }
}
