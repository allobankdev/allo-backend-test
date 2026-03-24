package com.allobank.test.controller;

import com.allobank.test.service.FinanceDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final FinanceDataService financeDataService;

    public FinanceDataController(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }

    @GetMapping("/{resourceType}")
    public Map<String, Object> getFinanceData(@PathVariable String resourceType) {
        return Map.of(
                "resourceType", resourceType,
                "data", financeDataService.findByResourceType(resourceType)
        );
    }
}
