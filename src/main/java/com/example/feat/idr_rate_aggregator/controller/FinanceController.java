package com.example.feat.idr_rate_aggregator.controller;

import com.example.feat.idr_rate_aggregator.service.financeDataStore.DataStoreService;
import com.example.feat.idr_rate_aggregator.service.financeDataStore.IDRDataFetcher;
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

    private final DataStoreService dataStoreService;
    private final Map<String, IDRDataFetcher> fetcherStrategyMap;

    public FinanceController(DataStoreService dataStoreService, List<IDRDataFetcher> fetchers) {
        this.dataStoreService = dataStoreService;
        this.fetcherStrategyMap = fetchers.stream()
                .collect(Collectors.toMap(IDRDataFetcher::getResourceKey, Function.identity()));
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@PathVariable String resourceType) {
        if (!fetcherStrategyMap.containsKey(resourceType)) {
            return ResponseEntity.badRequest().body("Invalid resourceType: " + resourceType);
        }

        Object data = dataStoreService.getData(resourceType);

        if (data == null) {
            return ResponseEntity.internalServerError().body("Data for resource type " + resourceType + " not loaded.");
        }

        return ResponseEntity.ok(data);
    }
}
