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

    // ---------------------------------------------------------------------------
    // SPECIAL SET IDS
    // ---------------------------------------------------------------------------
    // These sets need reverse-holo catalog completion after their normal JSON
    // card imports run.
    // ---------------------------------------------------------------------------

    private static final UUID LEGENDARY_COLLECTION_SET_ID =
            UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    private static final UUID EXPEDITION_SET_ID =
            UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");

    private static final UUID AQUAPOLIS_SET_ID =
            UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee");

    private static final UUID SKYRIDGE_SET_ID =
            UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");


    // ---------------------------------------------------------------------------
    // REVERSE HOLO CATALOG RULES
    // ---------------------------------------------------------------------------
    // These ranges define which cards should have reverse-holo variants.
    // The expected totals are used in the import response so developers can verify
    // the catalog after a fresh DB setup or repeated import run.
    // ---------------------------------------------------------------------------

    private static final int LEGENDARY_REVERSE_HOLO_EXPECTED_TOTAL = 110;

    private static final int EXPEDITION_REVERSE_HOLO_FIRST_CARD = 1;
    private static final int EXPEDITION_REVERSE_HOLO_LAST_CARD = 159;
    private static final int EXPEDITION_REVERSE_HOLO_EXPECTED_TOTAL = 159;

    private static final int AQUAPOLIS_REVERSE_HOLO_FIRST_CARD = 1;
    private static final int AQUAPOLIS_REVERSE_HOLO_LAST_CARD = 147;
    private static final int AQUAPOLIS_REVERSE_HOLO_EXPECTED_TOTAL = 147;

    private static final int SKYRIDGE_REVERSE_HOLO_FIRST_CARD = 1;
    private static final int SKYRIDGE_REVERSE_HOLO_LAST_CARD = 150;
    private static final int SKYRIDGE_REVERSE_HOLO_EXPECTED_TOTAL = 150;


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

                // ---------------------------------------------------------------------------
                // REVERSE HOLO VARIANT CREATION
                // ---------------------------------------------------------------------------
                // These calls complete reverse-holo variants after the base card imports.
                // They are safe to rerun because the service checks whether each variant
                // already exists before creating it.
                // ---------------------------------------------------------------------------

                int legendaryReverseHolosCreated = importService.ensureReverseHoloVariantsForAllCards(
                        LEGENDARY_COLLECTION_SET_ID
                );

                int expeditionReverseHolosCreated = importService.ensureReverseHoloVariantsForNumberRange(
                        EXPEDITION_SET_ID,
                        EXPEDITION_REVERSE_HOLO_FIRST_CARD,
                        EXPEDITION_REVERSE_HOLO_LAST_CARD
                );

                int aquapolisReverseHolosCreated = importService.ensureReverseHoloVariantsForNumberRange(
                        AQUAPOLIS_SET_ID,
                        AQUAPOLIS_REVERSE_HOLO_FIRST_CARD,
                        AQUAPOLIS_REVERSE_HOLO_LAST_CARD
                );

                int skyridgeReverseHolosCreated = importService.ensureReverseHoloVariantsForNumberRange(
                        SKYRIDGE_SET_ID,
                        SKYRIDGE_REVERSE_HOLO_FIRST_CARD,
                        SKYRIDGE_REVERSE_HOLO_LAST_CARD
                );


                // ---------------------------------------------------------------------------
                // REVERSE HOLO VARIANT REPORTING
                // ---------------------------------------------------------------------------
                // Count the final database state after creation. This helps developers tell
                // the difference between:
                // - created this run: 0 because the data already existed
                // - total now: too low because catalog data is missing
                // ---------------------------------------------------------------------------

                int legendaryReverseHoloTotal = importService.countReverseHoloVariantsForAllCards(
                        LEGENDARY_COLLECTION_SET_ID
                );

                int expeditionReverseHoloTotal = importService.countReverseHoloVariantsForNumberRange(
                        EXPEDITION_SET_ID,
                        EXPEDITION_REVERSE_HOLO_FIRST_CARD,
                        EXPEDITION_REVERSE_HOLO_LAST_CARD
                );

                int aquapolisReverseHoloTotal = importService.countReverseHoloVariantsForNumberRange(
                        AQUAPOLIS_SET_ID,
                        AQUAPOLIS_REVERSE_HOLO_FIRST_CARD,
                        AQUAPOLIS_REVERSE_HOLO_LAST_CARD
                );

                int skyridgeReverseHoloTotal = importService.countReverseHoloVariantsForNumberRange(
                        SKYRIDGE_SET_ID,
                        SKYRIDGE_REVERSE_HOLO_FIRST_CARD,
                        SKYRIDGE_REVERSE_HOLO_LAST_CARD
                );


                // ---------------------------------------------------------------------------
                // IMPORT SUMMARY RESPONSE
                // ---------------------------------------------------------------------------
                // Report both values:
                // - created this run
                // - total now in database
                // ---------------------------------------------------------------------------

                result.append("Legendary Collection reverse holos: created this run ")
                        .append(legendaryReverseHolosCreated)
                        .append(", total now ")
                        .append(legendaryReverseHoloTotal)
                        .append(" / expected ")
                        .append(LEGENDARY_REVERSE_HOLO_EXPECTED_TOTAL)
                        .append("\n");

                result.append("Expedition reverse holos: created this run ")
                        .append(expeditionReverseHolosCreated)
                        .append(", total now ")
                        .append(expeditionReverseHoloTotal)
                        .append(" / expected ")
                        .append(EXPEDITION_REVERSE_HOLO_EXPECTED_TOTAL)
                        .append("\n");

                result.append("Aquapolis reverse holos: created this run ")
                        .append(aquapolisReverseHolosCreated)
                        .append(", total now ")
                        .append(aquapolisReverseHoloTotal)
                        .append(" / expected ")
                        .append(AQUAPOLIS_REVERSE_HOLO_EXPECTED_TOTAL)
                        .append("\n");

                result.append("Skyridge reverse holos: created this run ")
                        .append(skyridgeReverseHolosCreated)
                        .append(", total now ")
                        .append(skyridgeReverseHoloTotal)
                        .append(" / expected ")
                        .append(SKYRIDGE_REVERSE_HOLO_EXPECTED_TOTAL)
                        .append("\n");

                return result.toString();
                }
}
