package com.allobankdev.exchangrate.controller;

import com.allobankdev.exchangrate.service.store.DataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
public class ExchangeRateController {
    private final DataStore store;

    public ExchangeRateController(DataStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> get(@PathVariable String resourceType) {
        return ResponseEntity.ok(store.get(resourceType));
    }
}
