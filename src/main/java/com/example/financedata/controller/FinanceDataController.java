package com.example.financedata.controller;

import com.example.financedata.store.ImmutableFinanceStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final ImmutableFinanceStore store;

    public FinanceDataController(ImmutableFinanceStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getResource(@PathVariable String resourceType) {
        if (!store.isLoaded()) {
            return ResponseEntity.status(503).body("Data not loaded yet.");
        }
        Object data = store.get(resourceType);
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }
}
