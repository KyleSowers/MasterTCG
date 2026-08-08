package com.mastertcg.controller;

import java.util.List;
import java.util.UUID;

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

        // ---------------------------------------------------------------------------
        // FULL CATALOG IMPORT
        // ---------------------------------------------------------------------------

        @PostMapping("/pokemon/all")
                public String importAllPokemonSets() {
                StringBuilder result = new StringBuilder();

                result.append(importPokemonSet("base1")).append("\n");
                result.append(importPokemonSet("base2")).append("\n");
                result.append(importPokemonSet("base3")).append("\n");
                result.append(importPokemonSet("base5")).append("\n");
                result.append(importPokemonSet("gym1")).append("\n");
                result.append(importPokemonSet("gym2")).append("\n");
                result.append(importPokemonSet("neo1")).append("\n");
                result.append(importPokemonSet("neo2")).append("\n");
                result.append(importPokemonSet("neo3")).append("\n");
                result.append(importPokemonSet("neo4")).append("\n");
                result.append(importPokemonSet("base6")).append("\n");
                result.append(importPokemonSet("ecard1")).append("\n");
                result.append(importPokemonSet("ecard2")).append("\n");
                result.append(importPokemonSet("ecard3")).append("\n");

                int legendaryReverseHolos = importService.ensureReverseHoloVariantsForAllCards(
                        UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc")
                );

                int expeditionReverseHolos = importService.ensureReverseHoloVariantsForNumberRange(
                        UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"),
                        1,
                        159
                );

                int aquapolisReverseHolos = importService.ensureReverseHoloVariantsForNumberRange(
                        UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee"),
                        1,
                        147
                );

                int skyridgeReverseHolos = importService.ensureReverseHoloVariantsForNumberRange(
                        UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"),
                        1,
                        150
                );

                result.append("Legendary Collection reverse holos created: ")
                        .append(legendaryReverseHolos)
                        .append("\n");

                result.append("Expedition reverse holos created: ")
                        .append(expeditionReverseHolos)
                        .append("\n");

                result.append("Aquapolis reverse holos created: ")
                        .append(aquapolisReverseHolos)
                        .append("\n");

                result.append("Skyridge reverse holos created: ")
                        .append(skyridgeReverseHolos)
                        .append("\n");

                return result.toString();
                }
}
