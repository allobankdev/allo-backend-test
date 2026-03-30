package com.allobank.test.controller;

import com.allobank.test.service.DataStoreService;
import com.allobank.test.strategy.DataFetcherStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final Map<String, DataFetcherStrategy> strategies;
    private final DataStoreService dataStoreService;

    /**
     * Constraint A mandates mapping lookup, injected dynamically.
     * We accept a Map of string bean-names to Strategy, then map it by 
     * resourceTypes internally if necessary (or we can just filter).
     */
    @Autowired
    public FinanceController(Map<String, DataFetcherStrategy> strategies, 
                             DataStoreService dataStoreService) {
        this.strategies = strategies;
        this.dataStoreService = dataStoreService;
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@PathVariable String resourceType) {
        // Step 1: Constraint A - Use injected Strategy to validate dynamic types 
        // avoiding traditional if/else mechanisms.
        DataFetcherStrategy selectedStrategy = strategies.values().stream()
                .filter(s -> resourceType.equals(s.getResourceType()))
                .findFirst()
                .orElse(null);

        if (selectedStrategy == null) {
            return ResponseEntity.notFound().build();
        }

        // Step 2: Constraint C - Extract data directly from the in-memory cache 
        // to prevent unnecessary and costly API hits on each request.
        Object preloadedData = dataStoreService.retrieveData(selectedStrategy.getResourceType());
        
        if (preloadedData != null) {
            return ResponseEntity.ok(preloadedData);
        } else {
            return ResponseEntity.internalServerError().body("Data was not retrieved or cached correctly during startup.");
        }
    }
}
