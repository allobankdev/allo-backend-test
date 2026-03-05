package com.allobank.finance.controller;

import com.allobank.finance.model.FinanceDataResponse;
import com.allobank.finance.service.FinanceDataService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
        if (!financeDataService.isKnownResourceType(resourceType)) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Resource type tidak dikenal",
                    "resourceType", resourceType,
                    "validOptions", new String[]{
                            "latest_idr_rates",
                            "historical_idr_usd",
                            "supported_currencies"
                    }
            ));
        }

        return financeDataService.getDataForResource(resourceType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(503).body(null));
    }
}