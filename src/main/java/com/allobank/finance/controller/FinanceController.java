package com.allobank.finance.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.finance.store.IDRDataStore;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final IDRDataStore dataStore;

    public FinanceController(IDRDataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Retrieves IDR currency conversion data for the specified resource type
     * 
     * @param resourceType one of: latest_idr_rates, historical_idr_usd, supported_currencies
     * @return the cached data for the requested resource type
     */
    @GetMapping("/data/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@PathVariable String resourceType) {
        if (!dataStore.contains(resourceType)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Resource type not found: " + resourceType));
        }

        Object data = dataStore.get(resourceType);
        return ResponseEntity.ok(data);
    }

    /**
     * Simple error response wrapper
     */
    public static class ErrorResponse {
        public String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
