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


    // ---------------------------------------------------------------------------
// REVERSE HOLO VARIANT REPAIR / CATALOG COMPLETION HELPERS
// ---------------------------------------------------------------------------

    @Transactional
        public int ensureReverseHoloVariantsForAllCards(UUID setId) {
            int variantsCreated = 0;

            List<CardEntity> cards = cardRepository.findBySet_IdOrderByCardNumberAsc(setId);

            for (CardEntity card : cards) {
                boolean alreadyExists = cardVariantRepository
                        .findByCard_IdAndFinish(card.getId(), CardFinish.REVERSE_HOLO)
                        .isPresent();

                if (alreadyExists) {
                    continue;
                }

                CardVariantEntity reverseHoloVariant = new CardVariantEntity();
                reverseHoloVariant.setId(UUID.randomUUID());
                reverseHoloVariant.setCard(card);
                reverseHoloVariant.setFinish(CardFinish.REVERSE_HOLO);

                cardVariantRepository.save(reverseHoloVariant);
                variantsCreated++;
            }

            return variantsCreated;
        }

    @Transactional
        public int ensureReverseHoloVariantsForNumberRange(
                UUID setId,
                int firstCardNumber,
                int lastCardNumber
        ) {
            int variantsCreated = 0;

            List<CardEntity> cards = cardRepository.findBySet_IdOrderByCardNumberAsc(setId);

            for (CardEntity card : cards) {
                Integer numericCardNumber = getNumericCardNumber(card.getCardNumber());

                if (numericCardNumber == null) {
                    continue;
                }

                if (numericCardNumber < firstCardNumber || numericCardNumber > lastCardNumber) {
                    continue;
                }

                boolean alreadyExists = cardVariantRepository
                        .findByCard_IdAndFinish(card.getId(), CardFinish.REVERSE_HOLO)
                        .isPresent();

                if (alreadyExists) {
                    continue;
                }

                CardVariantEntity reverseHoloVariant = new CardVariantEntity();
                reverseHoloVariant.setId(UUID.randomUUID());
                reverseHoloVariant.setCard(card);
                reverseHoloVariant.setFinish(CardFinish.REVERSE_HOLO);

                cardVariantRepository.save(reverseHoloVariant);
                variantsCreated++;
            }

            return variantsCreated;
        }

        // ---------------------------------------------------------------------------
        // REVERSE HOLO VARIANT REPORTING HELPERS
        // ---------------------------------------------------------------------------
        // These methods do not create or modify card data.
        // They only count existing reverse-holo variants so import endpoints can report:
        // 1. how many reverse-holo variants were created during the current run
        // 2. how many reverse-holo variants now exist in the database after the run
        // ---------------------------------------------------------------------------

        @Transactional(readOnly = true)
        public int countReverseHoloVariantsForAllCards(UUID setId) {
            int reverseHoloCount = 0;

            List<CardEntity> cards = cardRepository.findBySet_IdOrderByCardNumberAsc(setId);

            for (CardEntity card : cards) {
                boolean exists = cardVariantRepository
                        .findByCard_IdAndFinish(card.getId(), CardFinish.REVERSE_HOLO)
                        .isPresent();

                if (exists) {
                    reverseHoloCount++;
                }
            }

            return reverseHoloCount;
        }

        @Transactional(readOnly = true)
            public int countReverseHoloVariantsForNumberRange(
                    UUID setId,
                    int firstCardNumber,
                    int lastCardNumber
            ) {
                int reverseHoloCount = 0;

                List<CardEntity> cards = cardRepository.findBySet_IdOrderByCardNumberAsc(setId);

                for (CardEntity card : cards) {
                    Integer numericCardNumber = getNumericCardNumber(card.getCardNumber());

                    if (numericCardNumber == null) {
                        continue;
                    }

                    if (numericCardNumber < firstCardNumber || numericCardNumber > lastCardNumber) {
                        continue;
                    }

                    boolean exists = cardVariantRepository
                            .findByCard_IdAndFinish(card.getId(), CardFinish.REVERSE_HOLO)
                            .isPresent();

                    if (exists) {
                        reverseHoloCount++;
                    }
                }

                return reverseHoloCount;
            }

        // ---------------------------------------------------------------------------
        // CARD NUMBER PARSING HELPERS
        // ---------------------------------------------------------------------------
        // Some Pokémon card numbers are strings like "106/105".
        // Reverse-holo rules only need the leading numeric card number.
        // ---------------------------------------------------------------------------

        private Integer getNumericCardNumber(String cardNumber) {
            if (cardNumber == null || cardNumber.isBlank()) {
                return null;
            }

            String digitsOnly = cardNumber.replaceAll("[^0-9]", "");

            if (digitsOnly.isBlank()) {
                return null;
            }

            return Integer.parseInt(digitsOnly);
        }
    
}
