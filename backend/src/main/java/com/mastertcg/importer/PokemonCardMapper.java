package com.mastertcg.importer;

import com.mastertcg.model.CardEntity;
import org.springframework.stereotype.Component;

@Component
public class PokemonCardMapper {

    public CardEntity toCardEntity(PokemonCardImportDto dto) {
        CardEntity card = new CardEntity();

        card.setCardNumber(dto.number());
        card.setName(dto.name());
        card.setRarity(mapRarity(dto.rarity()));
        card.setArtist(dto.artist());

        if (dto.types() != null && !dto.types().isEmpty()) {
            card.setPrimaryType(dto.types().get(0));
        }

        if (dto.images() != null) {
            card.setImageSmallUrl(dto.images().small());
            card.setImageLargeUrl(dto.images().large());
        }

        return card;
    }

    private String mapRarity(String rarity) {
        if (rarity == null) {
            return "COMMON";
        }

        return switch (rarity) {
            case "Common" -> "COMMON";
            case "Uncommon" -> "UNCOMMON";
            case "Rare", "Rare Holo" -> "RARE";
            default -> rarity.toUpperCase().replace(" ", "_");
        };
    }
}
