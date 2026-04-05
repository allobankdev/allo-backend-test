package com.example.finance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance.service.InMemoryDataStore;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final InMemoryDataStore dataStore;

    public FinanceController(InMemoryDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/{resourceType}")
    public Object getData(@PathVariable String resourceType) {

        Object data = dataStore.get(resourceType);

        if (data == null) {
            throw new RuntimeException("Invalid resource type");
        }

        return data;
    }
}