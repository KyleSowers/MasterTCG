package com.mastertcg.controller;


import com.mastertcg.dto.CardDto;
import com.mastertcg.dto.SetDto;
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

    public SetController(SetRepository setRepository, CardRepository cardRepository) {
        this.setRepository = setRepository;
        this.cardRepository = cardRepository;
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
        return cardRepository.findBySet_IdOrderByCardNumberAsc(setId).stream()
                .map(c -> new CardDto(
                        c.getId(),
                        c.getCardNumber(),
                        c.getName(),
                        c.getRarity(),
                        c.isReverseHolo()
                ))
                .toList();
    }
}
