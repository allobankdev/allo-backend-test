package com.prasetyahs.allo.finance.controller;

import com.prasetyahs.allo.finance.model.ApiResponse;
import com.prasetyahs.allo.finance.store.InMemoryDataStore;
import com.prasetyahs.allo.finance.strategy.IDRDataFetcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final Map<String, IDRDataFetcher> strategies;
    private final InMemoryDataStore dataStore;

    public FinanceController(Map<String, IDRDataFetcher> strategies, InMemoryDataStore dataStore) {
        this.strategies = strategies;
        this.dataStore = dataStore;
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<ApiResponse<Object>> getFinanceData(@PathVariable String resourceType) {
        if (!strategies.containsKey(resourceType)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Resource type tidak ditemukan: " + resourceType));
        }

        IDRDataFetcher strategy = strategies.get(resourceType);
        Object data = strategy.retrieveData(dataStore);

        if (data == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.error("Data gagal dimuat atau belum tersedia untuk: " + resourceType));
        }

        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
