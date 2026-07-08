package com.mastertcg.importer;

import com.mastertcg.model.CardEntity;
import com.mastertcg.model.CardFinish;
import com.mastertcg.model.CardVariantEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

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

    public CardVariantEntity toVariantEntity(PokemonCardImportDto dto, CardEntity card) {
    CardVariantEntity variant = new CardVariantEntity();

    variant.setId(UUID.randomUUID());
    variant.setCard(card);
    variant.setFinish(mapFinish(dto.rarity()));

    return variant;
    }

    private CardFinish mapFinish(String rarity) {
        if (rarity == null) {
            return CardFinish.NORMAL;
        }

        return switch (rarity) {
            case "Rare Holo" -> CardFinish.HOLO;
            default -> CardFinish.NORMAL;
        };
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
