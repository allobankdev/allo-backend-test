package com.allobank.frankfurter_aggregator.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.springframework.http.ResponseEntity;


import com.allobank.frankfurter_aggregator.service.FinanceDataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/finance/data")
@RequiredArgsConstructor
public class FinanceDataController {
    
    private final FinanceDataService financeDataService;
    
    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
        log.info("Received request for resource type: {}", resourceType);
        
        try {
            Object data = financeDataService.getFinanceData(resourceType);
            return ResponseEntity.ok(data);
        } catch (IllegalArgumentException e) {
            log.error("Invalid resource type: {}", resourceType);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid resource type. Supported types: latest_idr_rates, historical_idr_usd, supported_currencies"));
        } catch (IllegalStateException e) {
            log.warn("Application not ready: {}", e.getMessage());
            return ResponseEntity.status(503)
                    .body(Map.of("error", "Service temporarily unavailable. Application is starting up."));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Internal server error"));
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        boolean isReady = financeDataService.getStrategies().size() == 3;
        
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "strategiesLoaded", financeDataService.getStrategies().size(),
                "serviceReady", isReady
        ));
    }
}