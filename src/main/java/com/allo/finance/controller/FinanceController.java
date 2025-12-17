package com.allo.finance.controller;

import com.allo.finance.service.FinanceDataService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceDataService service;

    public FinanceController(FinanceDataService service) {
        this.service = service;
    }

    @GetMapping("/{resourceType}")
    public Object get(@PathVariable String resourceType) {
        Object data = service.get(resourceType);
        if (data == null) {
            throw new IllegalArgumentException("Invalid resource type");
        }
        return data;
    }
}
