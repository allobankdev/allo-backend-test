package com.example.frankfurter.controller;

import com.example.frankfurter.store.FinanceDataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final FinanceDataStore store;

    public FinanceDataController(FinanceDataStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<List<?>> getFinanceData(@PathVariable String resourceType) {
        List<?> data = store.getData(resourceType);
        return ResponseEntity.ok(data);
    }
}
