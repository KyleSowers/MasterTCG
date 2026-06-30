package com.mastertcg.controller;

import com.mastertcg.dto.OwnedCardDto;
import com.mastertcg.model.CardVariantEntity;
import com.mastertcg.model.UserCardEntity;
import com.mastertcg.repository.CardVariantRepository;
import com.mastertcg.repository.UserCardRepository;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-cards")
public class UserCardController {

    private static final UUID DEMO_USER_ID =
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    private final UserCardRepository userCardRepository;
    private final CardVariantRepository cardVariantRepository;

    public UserCardController(UserCardRepository userCardRepository,
                              CardVariantRepository cardVariantRepository) {
        this.userCardRepository = userCardRepository;
        this.cardVariantRepository = cardVariantRepository;
    }

    @GetMapping
    public List<OwnedCardDto> getOwnedCards() {
        return userCardRepository.findByUserId(DEMO_USER_ID).stream()
                .map(uc -> new OwnedCardDto(
                        uc.getCardVariant().getId(),
                        uc.getOwnedCount()
                ))
                .toList();
    }

    @PostMapping("/{cardVariantId}/toggle")
    public OwnedCardDto toggleOwned(@PathVariable UUID cardVariantId) {
        UserCardEntity userCard = userCardRepository
                .findByUserIdAndCardVariant_Id(DEMO_USER_ID, cardVariantId)
                .orElseGet(() -> {
                    CardVariantEntity variant = cardVariantRepository.findById(cardVariantId)
                            .orElseThrow();

                    UserCardEntity newUserCard = new UserCardEntity();
                    newUserCard.setId(UUID.randomUUID());
                    newUserCard.setUserId(DEMO_USER_ID);
                    newUserCard.setCardVariant(variant);
                    newUserCard.setOwnedCount(0);
                    newUserCard.setUpdatedAt(OffsetDateTime.now());
                    return newUserCard;
                });

        userCard.setOwnedCount(userCard.getOwnedCount() > 0 ? 0 : 1);
        userCard.setUpdatedAt(OffsetDateTime.now());

        UserCardEntity saved = userCardRepository.save(userCard);

        return new OwnedCardDto(
                saved.getCardVariant().getId(),
                saved.getOwnedCount()
        );
    }
}
