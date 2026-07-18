package com.mastertcg.importer;

import java.util.UUID;

public record PokemonSetImportConfig(
    String path,
    UUID setId,
    String displayName

) {}
