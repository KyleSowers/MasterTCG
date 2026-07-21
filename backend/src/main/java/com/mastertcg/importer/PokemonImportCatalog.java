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
        )),
        Map.entry("gym1", new PokemonSetImportConfig(
                "data/pokemon/gym1.json",
                UUID.fromString("55555555-5555-5555-5555-555555555555"),
                "Gym Heroes"
        )),
        Map.entry("gym-heroes", new PokemonSetImportConfig(
                "data/pokemon/gym1.json",
                UUID.fromString("55555555-5555-5555-5555-555555555555"),
                "Gym Heroes"
        )),
        Map.entry("gym2", new PokemonSetImportConfig(
                "data/pokemon/gym2.json",
                UUID.fromString("66666666-6666-6666-6666-666666666666"),
                "Gym Challenge"
        )),
        Map.entry("gym-challenge", new PokemonSetImportConfig(
                "data/pokemon/gym2.json",
                UUID.fromString("66666666-6666-6666-6666-666666666666"),
                "Gym Challenge"
        )),
        Map.entry("neo1", new PokemonSetImportConfig(
                "data/pokemon/neo1.json",
                UUID.fromString("77777777-7777-7777-7777-777777777777"),
                "Neo Genesis"
        )),
        Map.entry("neo-genesis", new PokemonSetImportConfig(
                "data/pokemon/neo1.json",
                UUID.fromString("77777777-7777-7777-7777-777777777777"),
                "Neo Genesis"
        )),
        Map.entry("neo2", new PokemonSetImportConfig(
                "data/pokemon/neo2.json",
                UUID.fromString("88888888-8888-8888-8888-888888888888"),
                "Neo Discovery"
        )),
        Map.entry("neo-discovery", new PokemonSetImportConfig(
                "data/pokemon/neo2.json",
                UUID.fromString("88888888-8888-8888-8888-888888888888"),
                "Neo Discovery"
        )),
        Map.entry("neo3", new PokemonSetImportConfig(
                "data/pokemon/neo3.json",
                UUID.fromString("99999999-9999-9999-9999-999999999999"),
                "Neo Revelation"
        )),
        Map.entry("neo-revelation", new PokemonSetImportConfig(
                "data/pokemon/neo3.json",
                UUID.fromString("99999999-9999-9999-9999-999999999999"),
                "Neo Revelation"
        )),
        Map.entry("neo4", new PokemonSetImportConfig(
                "data/pokemon/neo4.json",
                UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
                "Neo Destiny"
        )),
        Map.entry("neo-destiny", new PokemonSetImportConfig(
                "data/pokemon/neo4.json",
                UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
                "Neo Destiny"
        )),
        Map.entry("base6", new PokemonSetImportConfig(
                "data/pokemon/base6.json",
                UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"),
                "Legendary Collection"
        )),
        Map.entry("legendary-collection", new PokemonSetImportConfig(
                "data/pokemon/base6.json",
                UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"),
                "Legendary Collection"
        )),
        Map.entry("ecard1", new PokemonSetImportConfig(
                "data/pokemon/ecard1.json",
                UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"),
                "Expedition Base Set"
        )),
        Map.entry("expedition", new PokemonSetImportConfig(
                "data/pokemon/ecard1.json",
                UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"),
                "Expedition Base Set"
        ))
    );
       
    public Optional<PokemonSetImportConfig> findBySetCode(String setCode) {
        return Optional.ofNullable(imports.get(setCode.toLowerCase()));
    }
    

}
