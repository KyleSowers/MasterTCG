package com.mastertcg.controller;


import com.mastertcg.dto.CardDto;
import com.mastertcg.dto.SetDto;
import com.mastertcg.dto.CardVariantDto;
import com.mastertcg.model.CardVariantEntity;
import com.mastertcg.repository.CardVariantRepository;
import com.mastertcg.repository.CardRepository;
import com.mastertcg.repository.SetRepository;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sets")
public class SetController {

    private final SetRepository setRepository;
    private final CardRepository cardRepository;
    private final CardVariantRepository cardVariantRepository;

    public SetController(SetRepository setRepository, CardRepository cardRepository, CardVariantRepository cardVariantRepository) {
        this.setRepository = setRepository;
        this.cardRepository = cardRepository;
        this.cardVariantRepository = cardVariantRepository;
    }

    @GetMapping
    public List<SetDto> getSets() {
        return setRepository.findAll().stream()
                .map(s -> new SetDto(
                        s.getId(),
                        s.getName(),
                        s.getEra(),
                        s.getReleaseDate(),
                        s.getTotalCardsMain(),
                        s.getTotalCarsMaster()
                ))
                .toList();
    }

    @GetMapping("/{setId}/cards")
    public List<CardDto> getCards(@PathVariable UUID setId) {
        List<CardVariantEntity> variants = 
            cardVariantRepository.findByCard_Set_Id(setId);

        return cardRepository.findBySet_IdOrderByCardNumberAsc(setId).stream()
                .sorted((a, b) -> {
                        int aNumber = Integer.parseInt(a.getCardNumber());
                        int bNumber = Integer.parseInt(b.getCardNumber());
                        return Integer.compare(aNumber, bNumber);
                })
                .map(card -> new CardDto(
                        card.getId(),
                        card.getCardNumber(),
                        card.getName(),
                        card.getRarity(),
                        card.getImageSmallUrl(),
                        card.getImageLargeUrl(),
                        card.getPrimaryType(),
                        card.getArtist(),
                        variants.stream()
                                .filter(v -> v.getCard().getId().equals(card.getId()))
                                .map(v -> new CardVariantDto(
                                    v.getId(),
                                    v.getFinish()
                                ))
                                .toList()
                        // c.getFinish()
                ))
                .toList();
    }
}
