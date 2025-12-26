package com.allobank.finance.controller;

import com.allobank.finance.service.FinanceDataStore;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;  

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceDataStore store;

    public FinanceController(FinanceDataStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public Object getData(@PathVariable String resourceType) {
        Object data = store.get(resourceType);
        if (data == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Unknown resource type");
        }
        return data;
    }
}

