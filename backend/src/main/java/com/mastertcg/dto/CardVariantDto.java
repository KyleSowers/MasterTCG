package com.mastertcg.dto;

import com.mastertcg.model.CardFinish;
import java.util.UUID;

public record CardVariantDto(
        UUID id,
        CardFinish finish
) {}
