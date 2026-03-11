package com.allobank.idrrates.controller;

import com.allobank.idrrates.dto.ApiResponse;
import com.allobank.idrrates.exception.ResourceNotFoundException;
import com.allobank.idrrates.service.DataStoreService;
import com.allobank.idrrates.strategy.IDRDataFetcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final Map<String, IDRDataFetcher> strategyMap;
    private final DataStoreService dataStoreService;

    public FinanceController(List<IDRDataFetcher> fetchers, DataStoreService dataStoreService) {
        this.strategyMap = fetchers.stream()
                .collect(Collectors.toMap(IDRDataFetcher::getResourceType, Function.identity()));
        this.dataStoreService = dataStoreService;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<ApiResponse<?>> getData(@PathVariable String resourceType) {
        if (!strategyMap.containsKey(resourceType)) {
            throw new ResourceNotFoundException("Unknown resource type: " + resourceType);
        }
        List<?> data = dataStoreService.getData(resourceType);
        return ResponseEntity.ok(ApiResponse.of(resourceType, data));
    }
}
