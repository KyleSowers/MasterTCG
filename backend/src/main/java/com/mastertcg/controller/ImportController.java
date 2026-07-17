package com.mastertcg.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mastertcg.importer.PokemonCardImportDto;
import com.mastertcg.importer.PokemonImportService;
import com.mastertcg.importer.PokemonImportResult;

@RestController
@RequestMapping("/import")
public class ImportController {

    private final PokemonImportService importService;

    private final Map<String, PokemonSetImportConfig> pokemonImports = Map.of(
            "base1", new PokemonSetImportConfig(
                    "data/pokemon/base1.json",
                    UUID.fromString("11111111-1111-1111-1111-111111111111"),
                    "Base Set"
            ),
            "base2", new PokemonSetImportConfig(
                    "data/pokemon/base2.json",
                    UUID.fromString("22222222-2222-2222-2222-222222222222"),
                    "Jungle"
            ),
            "jungle", new PokemonSetImportConfig(
                    "data/pokemon/base2.json",
                    UUID.fromString("22222222-2222-2222-2222-222222222222"),
                    "Jungle"
            ),
            "base3", new PokemonSetImportConfig(
                    "data/pokemon/base3.json",
                    UUID.fromString("33333333-3333-3333-3333-333333333333"),
                    "Fossil"
            ),
            "fossil", new PokemonSetImportConfig(
                    "data/pokemon/base3.json",
                    UUID.fromString("33333333-3333-3333-3333-333333333333"),
                    "Fossil"
            )
    );

    public ImportController(PokemonImportService importService) {
        this.importService = importService;
    }

    @GetMapping("/test")
    public List<PokemonCardImportDto> testImport() {
        return importService.loadCardsFromJson("data/pokemon/base1.json");
    }

    @PostMapping("/pokemon/{setCode}")
    public PokemonImportResponse importPokemonSet(@PathVariable String setCode) {
        PokemonSetImportConfig config = pokemonImports.get(setCode);

        if (config == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No Pokemon import configured for set code: " + setCode
            );
        }

        PokemonImportResult result = importService.importCards(config.path(), config.setId());

        return new PokemonImportResponse(
                config.displayName(),
                config.displayName() + " import complete.",
                result.cardsProcessed(),
                result.cardsCreated(),
                result.cardsUpdated(),
                result.variantsCreated(),
                result.variantsSkipped()
        );
    }

    private record PokemonSetImportConfig(
            String path,
            UUID setId,
            String displayName
    ) {}

    private record PokemonImportResponse(
        String setName,
        String message,
        int cardsProcessed,
        int cardsCreated,
        int cardsUpdated,
        int variantsCreated,
        int variantsSkipped
    ) {}
}
