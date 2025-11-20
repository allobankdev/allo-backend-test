package com.nurmaya.allobank.idr_rate_aggregator.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nurmaya.allobank.idr_rate_aggregator.service.AggregatedDataStore;
import com.nurmaya.allobank.idr_rate_aggregator.strategy.IDRDataFetcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {
    private final AggregatedDataStore store;
    
    @Autowired
    private Map<String, IDRDataFetcher> strategyMap;

    public FinanceController(AggregatedDataStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {

        log.info("Received API Request for resourceType = {}", resourceType);

        if (!strategyMap.containsKey(resourceType)) {
            return ResponseEntity.badRequest().body("Invalid resourceType");
        }

        log.info("Serving cached data for resourceType = {}", resourceType);

        return ResponseEntity.ok(store.getData(resourceType));
    }
}
