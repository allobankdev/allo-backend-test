package com.tes.allo.controller;

import com.tes.allo.fetcher.InMemoryDataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final InMemoryDataStore store;

    public FinanceController(InMemoryDataStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getResource(@PathVariable String resourceType) {
        if (!store.isLoaded()) {
            return ResponseEntity.status(503).body("Data not loaded yet");
        }
        Object v = store.get(resourceType);
        if (v == null) {
            return ResponseEntity.badRequest().body("Unknown resourceType: " + resourceType);
        }
        return ResponseEntity.ok(v);
    }
}
