package com.allobank.financeaggregator.controller;

import com.allobank.financeaggregator.dto.ApiResponse;
import com.allobank.financeaggregator.exception.ResourceNotFoundException;
import com.allobank.financeaggregator.model.FinanceDataItem;
import com.allobank.financeaggregator.service.FinanceDataStore;
import com.allobank.financeaggregator.strategy.IDRDataFetcher;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final Map<String, IDRDataFetcher> strategies;
    private final FinanceDataStore dataStore;

    public FinanceDataController(Map<String, IDRDataFetcher> strategies, FinanceDataStore dataStore) {
        this.strategies = strategies;
        this.dataStore = dataStore;
    }

    @GetMapping("/{resourceType}")
    public ApiResponse<List<FinanceDataItem<?>>> getData(@PathVariable String resourceType) {
        IDRDataFetcher fetcher = java.util.Optional.ofNullable(strategies.get(resourceType))
                .orElseThrow(() -> new ResourceNotFoundException("Unknown resourceType: " + resourceType));
        return ApiResponse.success(dataStore.get(resourceType));
    }
}
