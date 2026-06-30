package com.mastertcg.dto;

// import com.mastertcg.model.CardFinish;
import java.util.UUID;
import java.util.List;

public record CardDto (
    UUID id,
    String cardNumber,
    String name,
    String rarity,
    List<CardVariantDto> variants
    // CardFinish finish
) {}
