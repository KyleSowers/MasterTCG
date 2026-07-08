package com.mastertcg.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class PokemonImportService {

    private final ObjectMapper objectMapper = new ObjectMapper();

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
    
}
