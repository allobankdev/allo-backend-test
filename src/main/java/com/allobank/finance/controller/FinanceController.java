package com.allobank.finance.controller;

import com.allobank.finance.service.InMemoryFinanceStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final InMemoryFinanceStore inMemoryFinanceStore;

    public FinanceController(InMemoryFinanceStore inMemoryFinanceStore) {
        this.inMemoryFinanceStore = inMemoryFinanceStore;
    }

    @GetMapping("/{resourceType}")
    public Object getData(@PathVariable String resourceType){

        return inMemoryFinanceStore.getData(resourceType);
    }
}
