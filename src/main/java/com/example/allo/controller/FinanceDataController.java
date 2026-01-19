package com.example.allo.controller;

import com.example.allo.service.FinanceDataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final FinanceDataStore store;

    public FinanceDataController(FinanceDataStore store) {
        this.store = store;
    }

    /**
     * For Resource Type Use This :
     * 1. latest_idr_rates
     * 2. historical_idr_usd
     * 3. supported_currencies
     * @param resourceType resource type
     */
    @GetMapping("/{resourceType}")
    public ResponseEntity<List<Object>> getData(
            @PathVariable String resourceType) {

        Object data = store.get(resourceType);
        if (data == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(List.of(data));
    }
}

