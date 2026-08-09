package com.mastertcg.profile;

import com.mastertcg.model.CollectionProfileEntity;
import com.mastertcg.repository.CollectionProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

@Service
public class CollectionProfileService {

    // ---------------------------------------------------------------------------
    // TEMPORARY DEMO USER
    // ---------------------------------------------------------------------------
    // This placeholder user ID lets us save/load profiles before real accounts exist.
    // Later, this should be replaced by the authenticated user's ID.
    // ---------------------------------------------------------------------------

    private static final UUID DEMO_USER_ID =
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    private final CollectionProfileRepository collectionProfileRepository;

    public CollectionProfileService(CollectionProfileRepository collectionProfileRepository) {
        this.collectionProfileRepository = collectionProfileRepository;
    }


    // ---------------------------------------------------------------------------
    // DEMO PROFILE API METHODS
    // ---------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public Optional<CollectionProfileResponse> getDemoProfile() {
        return collectionProfileRepository.findByUserId(DEMO_USER_ID)
                .stream()
                .findFirst()
                .map(this::toResponse);
    }

    @Transactional
    public CollectionProfileResponse saveDemoProfile(CollectionProfileRequest request) {
        CollectionProfileEntity profile = collectionProfileRepository.findByUserId(DEMO_USER_ID)
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    CollectionProfileEntity newProfile = new CollectionProfileEntity();
                    newProfile.setUserId(DEMO_USER_ID);
                    return newProfile;
                });

        applyRequestToEntity(request, profile);

        CollectionProfileEntity savedProfile = collectionProfileRepository.save(profile);

        return toResponse(savedProfile);
    }


    // ---------------------------------------------------------------------------
    // MAPPING HELPERS
    // ---------------------------------------------------------------------------
    // These keep controller code clean by centralizing conversions between:
    // CollectionProfileRequest -> CollectionProfileEntity -> CollectionProfileResponse
    // ---------------------------------------------------------------------------

    private void applyRequestToEntity(
            CollectionProfileRequest request,
            CollectionProfileEntity profile
    ) {
        profile.setName(
                request.name() == null || request.name().isBlank()
                        ? "My Collection Profile"
                        : request.name()
        );

        profile.setCollectionStyle(
                request.collectionStyle() == null || request.collectionStyle().isBlank()
                        ? "CUSTOM"
                        : request.collectionStyle()
        );

        profile.setIncludeNormal(request.includeNormal());
        profile.setIncludeHolo(request.includeHolo());
        profile.setIncludeReverseHolo(request.includeReverseHolo());
        profile.setIncludeSpecialFinishes(request.includeSpecialFinishes());

        profile.setIncludeCommon(request.includeCommon());
        profile.setIncludeUncommon(request.includeUncommon());
        profile.setIncludeRare(request.includeRare());

        profile.setIncludeMainCards(request.includeMainCards());
        profile.setIncludeSecretCards(request.includeSecretCards());

        profile.setSelectedSetIds(
                request.selectedSetIds() == null
                        ? new LinkedHashSet<>()
                        : new LinkedHashSet<>(request.selectedSetIds())
        );
    }

    private CollectionProfileResponse toResponse(CollectionProfileEntity profile) {
        return new CollectionProfileResponse(
                profile.getId(),
                profile.getUserId(),

                profile.getName(),
                profile.getCollectionStyle(),

                profile.isIncludeNormal(),
                profile.isIncludeHolo(),
                profile.isIncludeReverseHolo(),
                profile.isIncludeSpecialFinishes(),

                profile.isIncludeCommon(),
                profile.isIncludeUncommon(),
                profile.isIncludeRare(),

                profile.isIncludeMainCards(),
                profile.isIncludeSecretCards(),

                profile.getSelectedSetIds(),

                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}
