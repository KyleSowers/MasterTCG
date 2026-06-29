package com.mastertcg.controller;

import com.mastertcg.dto.OwnedCardDto;
import com.mastertcg.model.CardEntity;
import com.mastertcg.model.UserCardEntity;
import com.mastertcg.repository.CardRepository;
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
    private final CardRepository cardRepository;

    public UserCardController(UserCardRepository userCardRepository,
                              CardRepository cardRepository) {
        this.userCardRepository = userCardRepository;
        this.cardRepository = cardRepository;
    }

    @GetMapping
    public List<OwnedCardDto> getOwnedCards() {
        return userCardRepository.findByUserId(DEMO_USER_ID).stream()
                .map(uc -> new OwnedCardDto(
                        uc.getCard().getId(),
                        uc.getOwnedCount()
                ))
                .toList();
    }

    @PostMapping("/{cardId}/toggle")
    public OwnedCardDto toggleOwned(@PathVariable UUID cardId) {
        UserCardEntity userCard = userCardRepository
                .findByUserIdAndCard_Id(DEMO_USER_ID, cardId)
                .orElseGet(() -> {
                    CardEntity card = cardRepository.findById(cardId)
                            .orElseThrow();

                    UserCardEntity newUserCard = new UserCardEntity();
                    newUserCard.setId(UUID.randomUUID());
                    newUserCard.setUserId(DEMO_USER_ID);
                    newUserCard.setCard(card);
                    newUserCard.setOwnedCount(0);
                    newUserCard.setUpdatedAt(OffsetDateTime.now());
                    return newUserCard;
                });

        userCard.setOwnedCount(userCard.getOwnedCount() > 0 ? 0 : 1);
        userCard.setUpdatedAt(OffsetDateTime.now());

        UserCardEntity saved = userCardRepository.save(userCard);

        return new OwnedCardDto(
                saved.getCard().getId(),
                saved.getOwnedCount()
        );
    }
}
