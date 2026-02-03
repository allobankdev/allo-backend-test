package com.example.allobank.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.allobank.cache.ExchangeRateCache;
import com.example.allobank.dto.ResourceResponseDTO;

@RestController
@RequestMapping("/api/finance/data")
public class ExchangeRateController {

    private final ExchangeRateCache cache;

    public ExchangeRateController(ExchangeRateCache cache) {
        this.cache = cache;
    }

    @GetMapping("/{resourceType}")
    public List<ResourceResponseDTO> getData(
            @PathVariable String resourceType
    ) {

        Object data = cache.get(resourceType);

        if (data == null) {
            throw new RuntimeException("Unsupported resource type");
        }

        return List.of(
                new ResourceResponseDTO(resourceType, data)
        );
    }
}

