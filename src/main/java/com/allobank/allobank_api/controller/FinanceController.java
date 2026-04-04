package com.allobank.allobank_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobank_api.service.FinanceService;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {
    private final FinanceService service;

    public FinanceController(FinanceService service) {
        this.service = service;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getData(@PathVariable String resourceType) {
        return ResponseEntity.ok(service.getData(resourceType));
    }
    
}
