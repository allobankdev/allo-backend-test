package com.finance.aggregator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.aggregator.service.FinanceDataService;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {
    private final FinanceDataService service;

    public FinanceController(FinanceDataService service) {
        this.service = service;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@PathVariable String resourceType) {
        Object data = service.getData(resourceType);
        if (data == null) return ResponseEntity.status(404).body("Resource type not found.");
        return ResponseEntity.ok(data);
    }
}