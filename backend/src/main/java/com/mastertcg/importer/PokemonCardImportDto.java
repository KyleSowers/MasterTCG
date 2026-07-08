package com.mastertcg.importer;


import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record PokemonCardImportDto(
    String id,
    String name,
    List<String> types,
    String number,
    String artist,
    String rarity,
    Images images
) {
    public record Images(
        String small,
        String large
    ) {
    }

}
