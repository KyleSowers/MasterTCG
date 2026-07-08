package com.mastertcg.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastertcg.model.CardEntity;
import com.mastertcg.model.SetEntity;
import com.mastertcg.repository.SetRepository;
import com.mastertcg.repository.CardRepository;
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

    public PokemonImportService(
        PokemonCardMapper mapper,
        CardRepository cardRepository,
        SetRepository setRepository
    ) {
        this.mapper = mapper;
        this.cardRepository = cardRepository;
        this.setRepository = setRepository;
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
            CardEntity card = mapper.toCardEntity(dto);
            card.setId(UUID.randomUUID());
            card.setSet(set);

            cardRepository.save(card);
            count++;
        }

        return count;
    }
    
}
