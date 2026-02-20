package com.allo.backendtest.controller;


import com.allo.backendtest.store.FinanceDataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceDataStore store;

    public FinanceController(FinanceDataStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<List<Object>> getData(
            @PathVariable String resourceType) {

        List<Object> data = store.get(resourceType);

        if (data.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(data);
    }
}
