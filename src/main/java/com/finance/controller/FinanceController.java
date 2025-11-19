package com.finance.controller;

import com.finance.service.AggregatedDataStore;
import com.finance.service.IDRDataFetcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {
    private final Map<String, IDRDataFetcher> fetcherMap;
    private final AggregatedDataStore store;

    public FinanceController(Map<String, IDRDataFetcher> fetcherMap, AggregatedDataStore store) {
        this.fetcherMap = fetcherMap;
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> get(@PathVariable String resourceType) {
        if (!store.contains(resourceType)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error","resource not found"));
        }
        return ResponseEntity.ok(store.get(resourceType));
    }
}
