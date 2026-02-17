package com.exchange.frankfurter.controller;

import com.exchange.frankfurter.dto.ApiResponse;
import com.exchange.frankfurter.store.FinanceDataStore;
import com.exchange.frankfurter.strategy.IDRDataFetcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceDataStore store;
    private final Map<String, IDRDataFetcher> strategyMap;

    public FinanceController(List<IDRDataFetcher> fetchers,
                             FinanceDataStore store) {

        this.store = store;

        // Build lookup map once
        this.strategyMap = fetchers.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcher::getResourceType,
                        f -> f
                ));
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<ApiResponse<?>> getData(
            @PathVariable String resourceType) {

        if (!strategyMap.containsKey(resourceType)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(resourceType, null));
        }

        Object data = store.get(resourceType);

        return ResponseEntity.ok(
                new ApiResponse<>(resourceType, data)
        );
    }

}
