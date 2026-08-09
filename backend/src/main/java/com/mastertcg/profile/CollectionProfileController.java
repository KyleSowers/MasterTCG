package com.mastertcg.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class CollectionProfileController {

    private final CollectionProfileService collectionProfileService;

    public CollectionProfileController(CollectionProfileService collectionProfileService) {
        this.collectionProfileService = collectionProfileService;
    }


    // ---------------------------------------------------------------------------
    // DEMO PROFILE ENDPOINTS
    // ---------------------------------------------------------------------------
    // These endpoints use a temporary demo user ID inside CollectionProfileService.
    // Later, this should be replaced by the authenticated user's account ID.
    // ---------------------------------------------------------------------------

    @GetMapping("/demo")
    public ResponseEntity<CollectionProfileResponse> getDemoProfile() {
        return collectionProfileService.getDemoProfile()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/demo")
    public CollectionProfileResponse saveDemoProfile(
            @RequestBody CollectionProfileRequest request
    ) {
        return collectionProfileService.saveDemoProfile(request);
    }
}
