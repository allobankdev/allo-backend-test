package com.example.idr.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.idr.service.store.FinanceDataStore;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final FinanceDataStore store;

    public FinanceDataController(FinanceDataStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public List<?> getData(@PathVariable String resourceType) {
        return store.get(resourceType);
    }
}
