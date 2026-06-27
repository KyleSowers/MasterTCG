package com.mastertcg.dto;

import com.mastertcg.model.CardFinish;
import java.util.UUID;

public record CardDto (
    UUID id,
    String cardNumber,
    String name,
    String rarity,
    CardFinish finish
) {}
