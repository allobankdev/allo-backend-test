package com.allobank.frankfurter.controller;

import com.allobank.frankfurter.service.InMemoryDataStore;
import com.allobank.frankfurter.service.strategy.IDRDataFetcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final InMemoryDataStore dataStore;
    private final Map<String, IDRDataFetcher> fetcherMap; // not strictly needed for serving data, but could be used to fetch on-demand if needed

    public FinanceController(InMemoryDataStore dataStore, Map<String, IDRDataFetcher> fetcherMap) {
        this.dataStore = dataStore;
        this.fetcherMap = fetcherMap;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> getData(@PathVariable String resourceType) {
        var result = dataStore.get(resourceType);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.getData());
    }
}