package com.example.idraggregator.controller;

import com.example.idraggregator.service.DataStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final DataStoreService store;

    public FinanceDataController(DataStoreService store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getResource(@PathVariable("resourceType") String resourceType) {
        if (!store.isInitialized()) {
            return ResponseEntity.status(503).body(Map.of("error", "Data not initialized"));
        }
        Object result = store.get(resourceType);
        if (result == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Unknown resourceType: " + resourceType));
        }
        return ResponseEntity.ok(result);
    }
}
