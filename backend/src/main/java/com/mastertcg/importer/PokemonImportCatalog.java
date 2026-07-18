package com.mastertcg.importer;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class PokemonImportCatalog {

    private final Map<String, PokemonSetImportConfig> imports = Map.ofEntries(
        Map.entry("base1", new PokemonSetImportConfig(
                "data/pokemon/base1.json",
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "Base Set"
        )),
        Map.entry("base2", new PokemonSetImportConfig(
                "data/pokemon/base2.json",
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "Jungle"
        )),
        Map.entry("jungle", new PokemonSetImportConfig(
                "data/pokemon/base2.json",
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "Jungle"
        )),
        Map.entry("base3", new PokemonSetImportConfig(
                "data/pokemon/base3.json",
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "Fossil"
        )),
        Map.entry("fossil", new PokemonSetImportConfig(
                "data/pokemon/base3.json",
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "Fossil"
        )),
        Map.entry("base5", new PokemonSetImportConfig(
                "data/pokemon/base5.json",
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                "Team Rocket"
        )),
        Map.entry("team-rocket", new PokemonSetImportConfig(
                "data/pokemon/base5.json",
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                "Team Rocket"
        )),
        Map.entry("rocket", new PokemonSetImportConfig(
                "data/pokemon/base5.json",
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                "Team Rocket"
        ))
    );
       
    public Optional<PokemonSetImportConfig> findBySetCode(String setCode) {
        return Optional.ofNullable(imports.get(setCode.toLowerCase()));
    }
    

}
