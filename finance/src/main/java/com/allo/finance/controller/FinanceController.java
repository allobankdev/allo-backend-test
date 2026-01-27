package com.allo.finance.controller;

import com.allo.finance.store.FinanceDataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceDataStore dataStore;

    public FinanceController(FinanceDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
        Object data = dataStore.getData(resourceType);

        if (data == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid resourceType: " + resourceType);
        }

        return ResponseEntity.ok(data);
    }
}
