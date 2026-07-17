package com.mastertcg.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastertcg.model.CardEntity;
import com.mastertcg.model.CardFinish;
import com.mastertcg.model.CardVariantEntity;
import com.mastertcg.model.SetEntity;
import com.mastertcg.repository.CardRepository;
import com.mastertcg.repository.CardVariantRepository;
import com.mastertcg.repository.SetRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public PokemonImportResult importCards(String path, UUID setId) {
        SetEntity set = setRepository.findById(setId).orElseThrow();
        var importedCards = loadCardsFromJson(path);

        int cardsProcessed = 0;
        int cardsCreated = 0;
        int cardsUpdated = 0;
        int variantsCreated = 0;
        int variantsSkipped = 0;

        for (PokemonCardImportDto dto : importedCards) {
            CardEntity mappedCard = mapper.toCardEntity(dto);

            var existingCard = cardRepository
                    .findBySet_IdAndCardNumber(set.getId(), mappedCard.getCardNumber());

            CardEntity card;

            if (existingCard.isPresent()) {
                card = existingCard.get();
                cardsUpdated++;
            } else {
                mappedCard.setId(UUID.randomUUID());
                mappedCard.setSet(set);
                card = mappedCard;
                cardsCreated++;
            }

            card.setName(mappedCard.getName());
            card.setRarity(mappedCard.getRarity());
            card.setArtist(mappedCard.getArtist());
            card.setPrimaryType(mappedCard.getPrimaryType());
            card.setImageSmallUrl(mappedCard.getImageSmallUrl());
            card.setImageLargeUrl(mappedCard.getImageLargeUrl());

            card = cardRepository.save(card);

            CardVariantEntity mappedVariant = mapper.toVariantEntity(dto, card);

            boolean variantExists = cardVariantRepository
                    .findByCard_IdAndFinish(card.getId(), mappedVariant.getFinish())
                    .isPresent();

            if (variantExists) {
                variantsSkipped++;
            } else {
                cardVariantRepository.save(mappedVariant);
                variantsCreated++;
            }

            cardsProcessed++;
        }

        return new PokemonImportResult(
                cardsProcessed,
                cardsCreated,
                cardsUpdated,
                variantsCreated,
                variantsSkipped
        );
    }
    
}
