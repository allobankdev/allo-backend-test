package com.aryaevan.allo.controller;

import com.aryaevan.allo.store.FinanceDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for finance data aggregation.
 * Exposes the endpoint for retrieving financial data based on resource type.
 * Serves data from the immutable in-memory store, not from the external API.
 */
@RestController
@RequestMapping("/api/finance")
public class FinanceController {
    
    private final FinanceDataStore dataStore;
    
    @Autowired
    public FinanceController(FinanceDataStore dataStore) {
        this.dataStore = dataStore;
    }
    
    /**
     * Retrieves financial data for the specified resource type from the in-memory store.
     * Data is cached on application startup and served directly without external API calls.
     * 
     * @param resourceType The type of resource to fetch (latest_idr_rates, historical_idr_usd, or supported_currencies)
     * @return The cached aggregated data for the resource
     */
    @GetMapping("/data/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
        Object data = dataStore.get(resourceType);
        
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(data);
    }
}
