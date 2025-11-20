package com.example.allow.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.allow.service.DataAggregationService;
import com.example.allow.strategy.IDRDataFetcher;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final DataAggregationService cache;
    private final Map<String, IDRDataFetcher> strategyMap;

    public FinanceDataController(DataAggregationService cache,
                                 List<IDRDataFetcher> strategies) {
        this.cache = cache;
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(IDRDataFetcher::getResourceKey, s -> s));
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getData(@PathVariable String resourceType) {
        if (!strategyMap.containsKey(resourceType)) {
            return ResponseEntity.badRequest().body("Invalid resourceType: " + resourceType);
        }

        if (!cache.isLoaded()) {
            return ResponseEntity.status(503).body("Data is still loading, please try again in a few seconds");
        }

        Object data = cache.get(resourceType);
        return ResponseEntity.ok(data);
    }
}
