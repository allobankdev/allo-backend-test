package com.allobank.finance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.allobank.finance.store.FinanceDataStore;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final FinanceDataStore dataStore;

    public FinanceController(FinanceDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@PathVariable String resourceType) {
        Object data = dataStore.get(resourceType);

        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }
}
