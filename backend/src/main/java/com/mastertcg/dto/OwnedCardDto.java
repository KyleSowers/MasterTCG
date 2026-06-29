package com.mastertcg.dto;

import java.util.UUID;

public record OwnedCardDto(
    UUID cardId,
    int ownedCount
) {} 