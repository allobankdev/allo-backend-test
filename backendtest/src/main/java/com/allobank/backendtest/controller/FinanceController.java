package com.allobank.backendtest.controller;

import com.allobank.backendtest.dto.ApiResponse;
import com.allobank.backendtest.fetcher.IDRDataFetcher;
import com.allobank.backendtest.service.ImmutableFinanceStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {
    private final ImmutableFinanceStore store;
    private final Map<String, IDRDataFetcher> fetcherMap;

    public FinanceController(ImmutableFinanceStore store, Map<String, IDRDataFetcher> fetcherMap) {
        this.store = store;
        this.fetcherMap = fetcherMap;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<ApiResponse> get(@PathVariable String resourceType) {

        if (!store.isInitialized()) {
            return ResponseEntity.status(503)
                    .body(ApiResponse.failure("Data is initializing, please try again later"));
        }

        var fetcher = fetcherMap.get(resourceType);
        if (fetcher == null) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.failure("Resource not found: " + resourceType));
        }

        var data = store.get(resourceType);
        return ResponseEntity.ok(ApiResponse.success("Success", data));
    }
}
