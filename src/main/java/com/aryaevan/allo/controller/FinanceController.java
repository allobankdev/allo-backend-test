package com.aryaevan.allo.controller;

import com.aryaevan.allo.strategy.IDRDataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * REST Controller for finance data aggregation.
 * Exposes the endpoint for retrieving financial data based on resource type.
 */
@RestController
@RequestMapping("/api/finance")
public class FinanceController {
    
    private final Map<String, IDRDataFetcher> strategyMap;
    
    @Autowired
    public FinanceController(Map<String, IDRDataFetcher> strategyMap) {
        this.strategyMap = strategyMap;
    }
    
    /**
     * Retrieves financial data for the specified resource type.
     * 
     * @param resourceType The type of resource to fetch (latest_idr_rates, historical_idr_usd, or supported_currencies)
     * @return The aggregated data for the resource
     */
    @GetMapping("/data/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
        // For now, just return the resourceType string
        return ResponseEntity.ok(resourceType);
    }
}
