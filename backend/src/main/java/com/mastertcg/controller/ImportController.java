package com.mastertcg.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mastertcg.importer.PokemonCardImportDto;
import com.mastertcg.importer.PokemonImportService;

@RestController
@RequestMapping("/import")
public class ImportController {

    private final PokemonImportService importService;

    public ImportController(PokemonImportService importService) {
        this.importService = importService;
    }

    @GetMapping("/test")
    public List<PokemonCardImportDto> testImport() {
        return importService.loadCardsFromJson("data/pokemon/base1.json");
    }

    @PostMapping("/pokemon/base1/preview")
    public String previewBaseImport() {
        int imported = importService.importCards(
                "data/pokemon/base1.json",
                UUID.fromString("11111111-1111-1111-1111-111111111111")
        );

        return "Imported " + imported + " cards.";
    }
}
