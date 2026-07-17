package com.mastertcg.importer;

public record PokemonImportResult(
        int cardsProcessed,
        int cardsCreated,
        int cardsUpdated,
        int variantsCreated,
        int variantsSkipped
) {}
