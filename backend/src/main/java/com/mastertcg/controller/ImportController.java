package com.mastertcg.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
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
}
