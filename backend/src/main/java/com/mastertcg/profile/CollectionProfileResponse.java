package com.mastertcg.profile;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record CollectionProfileResponse(
        UUID id,
        UUID userId,

        String name,
        String collectionStyle,

        boolean includeNormal,
        boolean includeHolo,
        boolean includeReverseHolo,
        boolean includeSpecialFinishes,

        boolean includeCommon,
        boolean includeUncommon,
        boolean includeRare,

        boolean includeMainCards,
        boolean includeSecretCards,

        Set<UUID> selectedSetIds,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
