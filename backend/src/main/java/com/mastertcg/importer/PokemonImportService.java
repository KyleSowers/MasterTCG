package com.mastertcg.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastertcg.model.CardEntity;
import com.mastertcg.model.SetEntity;
import com.mastertcg.model.CardVariantEntity;
import com.mastertcg.repository.SetRepository;
import com.mastertcg.repository.CardRepository;
import com.mastertcg.repository.CardVariantRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class PokemonImportService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PokemonCardMapper mapper;
    private final CardRepository cardRepository;
    private final SetRepository setRepository;
    private final CardVariantRepository cardVariantRepository;

    public PokemonImportService(
        PokemonCardMapper mapper,
        CardRepository cardRepository,
        SetRepository setRepository,
        CardVariantRepository cardVariantRepository
    ) {
        this.mapper = mapper;
        this.cardRepository = cardRepository;
        this.setRepository = setRepository;
        this.cardVariantRepository = cardVariantRepository;
    }

    public List<PokemonCardImportDto> loadCardsFromJson(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);

            try (InputStream inputStream = resource.getInputStream()) {
                return objectMapper.readValue(
                        inputStream,
                        new TypeReference<List<PokemonCardImportDto>>() {}
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Pokémon card data from " + path, e);
        }
    }

    public int importCards(String path, UUID setId) {
        SetEntity set = setRepository.findById(setId)
                .orElseThrow();

        var importedCards = loadCardsFromJson(path);

        int count = 0;

        for (PokemonCardImportDto dto : importedCards) {
            CardEntity card = cardRepository
                .findBySet_IdAndCardNumber(set.getId(), dto.number())
                .orElseGet(() -> {
                    CardEntity newCard = mapper.toCardEntity(dto);
                    newCard.setId(UUID.randomUUID());
                    newCard.setSet(set);
                    return newCard;
                });
            
                
            // Always refresh imported fields
            card.setName(dto.name());
            card.setRarity(mapper.toCardEntity(dto).getRarity());
            card.setArtist(dto.artist());

            if (dto.types() != null && !dto.types().isEmpty()) {
                card.setPrimaryType(dto.types().get(0));
            }

            if (dto.images() != null) {
                card.setImageSmallUrl(dto.images().small());
                card.setImageLargeUrl(dto.images().large());
            }

            cardRepository.save(card);

            CardVariantEntity mappedVariant = mapper.toVariantEntity(dto, card);

            if (cardVariantRepository
                    .findByCard_IdAndFinish(card.getId(), mappedVariant.getFinish())
                    .isEmpty()) {

                cardVariantRepository.save(mappedVariant);
            }

            count++;
        }

        return count;
    }
    
}
