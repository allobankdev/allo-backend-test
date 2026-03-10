package com.allobank.financeapi.controller;

import com.allobank.financeapi.model.enums.ResourceType;
import com.allobank.financeapi.service.FinanceDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceDataService financeDataService;

    public FinanceController(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@PathVariable String resourceType) {
        try {
            ResourceType type = ResourceType.fromValue(resourceType);
            return this.financeDataService.getData(type)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
