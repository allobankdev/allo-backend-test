package com.backend.allobank.controller;

import com.backend.allobank.dto.FinanceResponse;
import com.backend.allobank.store.InMemoryFinanceStore;
import com.backend.allobank.strategy.IDRDataFetcherRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final InMemoryFinanceStore store;
    private final IDRDataFetcherRegistry registry;

    public FinanceController(InMemoryFinanceStore store, IDRDataFetcherRegistry registry) {
        this.store = store;
        this.registry = registry;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<FinanceResponse> getData(@PathVariable String resourceType) {

        registry.getStrategy(resourceType);

        Object data = store.get(resourceType);

        if (data == null) {
            return ResponseEntity.notFound().build();
        }

        FinanceResponse response = new FinanceResponse(resourceType, data);

        return ResponseEntity.ok(response);
    }
}
