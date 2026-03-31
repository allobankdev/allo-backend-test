package com.allo.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allo.backend.store.DataStore;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final DataStore store;

    public FinanceController(DataStore store) {
        this.store = store;
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> get(@PathVariable String type) {
        return ResponseEntity.ok(store.get(type));
    }
}