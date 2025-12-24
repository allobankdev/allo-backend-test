package com.allo.finance.controller;

import com.allo.finance.service.FinanceDataStore;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceDataStore store;

    @GetMapping("/{resourceType}")
    public Object getFinanceData(@PathVariable String resourceType) {
        Object data = store.get(resourceType);

        if (data == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Unknown resourceType: " + resourceType
            );
        }

        return data;
    }

}