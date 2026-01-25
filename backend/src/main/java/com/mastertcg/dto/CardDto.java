package com.mastertcg.dto;

import java.util.UUID;

public record CardDto (
    UUID id,
    String cardNumber,
    String name,
    String rarity,
    boolean reverseHolo
) {}
