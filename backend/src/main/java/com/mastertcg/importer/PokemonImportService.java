package com.mastertcg.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastertcg.model.CardEntity;
import com.mastertcg.repository.CardRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class PokemonImportService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PokemonCardMapper mapper;
    private final CardRepository cardRepository;

    public PokemonImportService(
        PokemonCardMapper mapper,
        CardRepository cardRepository
    ) {
        this.mapper = mapper;
        this.cardRepository = cardRepository;
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

    public void importCards(String path) {

        var importedCards = loadCardsFromJson(path);

        for (PokemonCardImportDto dto : importedCards) {

            CardEntity card = mapper.toCardEntity(dto);

            //We'll save these in the next step.
            System.out.println(card.getCardNumber() + " - " + card.getName());
        }
    }
    
}
