package com.allo.bank.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allo.bank.dto.FinanceDataItem;
import com.allo.bank.service.FinanceDataService;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final FinanceDataService financeDataService;

    public FinanceDataController(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<List<FinanceDataItem>> getFinanceData(@PathVariable String resourceType) {
        return ResponseEntity.ok(financeDataService.getByResourceType(resourceType));
    }
}
