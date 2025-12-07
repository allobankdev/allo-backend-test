package com.example.allo_bank.controller;

import com.example.allo_bank.service.InMemoryStore;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/data")
public class CurrencyController {

    private final InMemoryStore store;

    public CurrencyController(InMemoryStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public Object getResource(@PathVariable String resourceType) {
        return store.get(resourceType);
    }
}
