package com.mastertcg.dto;

import java.time.LocalDate;
import java.util.UUID;

public record SetDto(
        UUID id,
        String name,
        String era,
        LocalDate releaseDate,
        int totalCardsMain,
        int totalCardsMaster
) {}

