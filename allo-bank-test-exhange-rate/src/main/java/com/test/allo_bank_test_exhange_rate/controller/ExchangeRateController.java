package com.test.allo_bank_test_exhange_rate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.allo_bank_test_exhange_rate.store.ImmutableDataStore;

@RestController
@RequestMapping("/api")
public class ExchangeRateController {
    
    private final ImmutableDataStore immutableDataStore;

    public ExchangeRateController(ImmutableDataStore immutableDataStore) {
        this.immutableDataStore = immutableDataStore;
    }

    @GetMapping("/finance/{resourceType}")
    public Object getExchangeRate(@PathVariable("resourceType") String resourceType) {
        return immutableDataStore.get(resourceType);
    }
}
