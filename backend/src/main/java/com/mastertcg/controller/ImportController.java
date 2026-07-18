package com.mastertcg.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mastertcg.importer.PokemonCardImportDto;
import com.mastertcg.importer.PokemonImportCatalog;
import com.mastertcg.importer.PokemonImportService;
import com.mastertcg.importer.PokemonImportResult;
import com.mastertcg.importer.PokemonSetImportConfig;

@RestController
@RequestMapping("/import")
public class ImportController {

    private final PokemonImportService importService;
    private final PokemonImportCatalog importCatalog;

    public ImportController(PokemonImportService importService, PokemonImportCatalog importCatalog) {
        this.importService = importService;
        this.importCatalog = importCatalog;
    }

    @GetMapping("/test")
    public List<PokemonCardImportDto> testImport() {
        return importService.loadCardsFromJson("data/pokemon/base1.json");
    }

    @PostMapping("/pokemon/{setCode}")
        public PokemonImportResponse importPokemonSet(@PathVariable String setCode) {
                PokemonSetImportConfig config = importCatalog.findBySetCode(setCode)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "No Pokemon import configured for set code: " + setCode
                        ));

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
