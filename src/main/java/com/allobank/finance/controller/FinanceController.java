package com.allobank.finance.controller;

import com.allobank.finance.service.FinanceService;
import com.allobank.finance.strategy.IDRDataFetcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final Map<String, IDRDataFetcher> dataFetchers;
    private final FinanceService financeService;

    public FinanceController(Map<String, IDRDataFetcher> dataFetchers, FinanceService financeService) {
        this.dataFetchers = dataFetchers;
        this.financeService = financeService;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<List<Map<String, Object>>> getFinanceData(@PathVariable String resourceType) {
        return Optional.ofNullable(dataFetchers.get(resourceType))
                .map(financeService::getFinanceData)
                .map(data -> data
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(List.of())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of()));
    }
}
