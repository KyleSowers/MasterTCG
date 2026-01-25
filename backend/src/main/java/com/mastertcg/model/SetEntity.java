package com.mastertcg.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;


@Entity
@Table(name = "sets")
public class SetEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String era;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "total_cards_main", nullable = false)
    private int totalCardsMain;

    @Column(name = "total_cards_master", nullable = false)
    private int totalCarsMaster;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEra() { return era; }
    public void setEra(String era) { this.era = era; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public int getTotalCardsMain() { return totalCardsMain; }
    public void setTotalCardsMain(int totalCardsMain) { this.totalCardsMain = totalCardsMain; }

    public int getTotalCarsMaster() { return totalCarsMaster; }
    public void setTotalCarsMaster(int totalCarsMaster) { this.totalCarsMaster = totalCarsMaster; }
}
