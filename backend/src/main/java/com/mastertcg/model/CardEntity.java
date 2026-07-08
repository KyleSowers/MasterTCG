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

    @Column(name = "image_small_url")
    private String imageSmallUrl;

    @Column(name = "image_large_url")
    private String imageLargeUrl;

    @Column(name = "primary_type")
    private String primaryType;

    @Column
    private String artist;

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

    public String getImageSmallUrl() { return imageSmallUrl; }
    public void setImageSmallUrl(String imageSmallUrl) { this.imageSmallUrl = imageSmallUrl; }

    public String getImageLargeUrl() { return imageLargeUrl; }
    public void setImageLargeUrl(String imageLargeUrl) { this.imageLargeUrl = imageLargeUrl; }

    public String getPrimaryType() { return primaryType; }
    public void setPrimaryType(String primaryType) { this.primaryType = primaryType; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
}
