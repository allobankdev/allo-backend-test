package com.allobank.finance.controller;

import com.allobank.finance.store.FinanceDataStore;
import com.allobank.finance.strategy.IDRDataFetcher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final FinanceDataStore dataStore;
    private final Map<String, IDRDataFetcher> fetcherMap;

    public FinanceDataController(
            FinanceDataStore dataStore,
            @Qualifier("idrDataFetcherMap") Map<String, IDRDataFetcher> fetcherMap) {
        this.dataStore = dataStore;
        this.fetcherMap = fetcherMap;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@PathVariable String resourceType) {
        if (!fetcherMap.containsKey(resourceType)) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message", "Invalid resource type: " + resourceType,
                            "supportedResourceTypes", fetcherMap.keySet()));
        }

        return dataStore.getData(resourceType)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.internalServerError()
                        .body(Map.of("message", "Finance data is not initialized for resource type: " + resourceType)));
    }
}
