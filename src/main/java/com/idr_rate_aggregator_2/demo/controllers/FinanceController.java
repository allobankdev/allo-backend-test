package com.idr_rate_aggregator_2.demo.controllers;

import com.idr_rate_aggregator_2.demo.dto.ApiErrorResponse;
import com.idr_rate_aggregator_2.demo.store.FinanceDataStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceDataStore dataStore;

    @Autowired
    public FinanceController(FinanceDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
        log.info("📤 Received request for resource type: {}", resourceType);

        // Cek apakah data store sudah siap
        if (!dataStore.isInitialized()) {
            log.warn("⚠️ Data store not yet initialized");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(createErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                            "Data is still initializing. Please try again in a few seconds."));
        }

        try {
            // Validasi resource type
            if (!isValidResourceType(resourceType)) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse(HttpStatus.BAD_REQUEST,
                                "Invalid resource type: " + resourceType +
                                        ". Supported types: latest_idr_rates, historical_idr_usd, supported_currencies"));
            }

            // Cek apakah data tersedia
            if (!dataStore.hasData(resourceType)) {
                log.error("❌ Data not available for resource type: {}", resourceType);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(createErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                                "Data for " + resourceType + " is not available. Please try again later."));
            }

            // Ambil data dari in-memory store (IMMUTABLE)
            Object data = dataStore.getData(resourceType);

            log.info("✅ Successfully retrieved data for: {}", resourceType);
            return ResponseEntity.ok(data);

        } catch (IllegalStateException e) {
            log.error("❌ Data not available: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(createErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage()));
        } catch (Exception e) {
            log.error("❌ Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                            "An unexpected error occurred"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        Map<String, Boolean> dataAvailability = dataStore.getAllData()
                .keySet()
                .stream()
                .collect(Collectors.toMap(key -> key, key -> true));

        HealthResponse response = HealthResponse.builder()
                .status(dataStore.isInitialized() ? "UP" : "INITIALIZING")
                .dataAvailability(dataAvailability)
                .initialized(dataStore.isInitialized())
                .build();

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/refresh")
//    public ResponseEntity<String> refresh() {
//        // Endpoint ini bisa digunakan untuk trigger manual refresh
//        // Tapi sesuai requirement, data hanya di-fetch sekali di startup
//        return ResponseEntity.ok("Data refresh is not allowed. Data is loaded only at application startup.");
//    }

    private boolean isValidResourceType(String resourceType) {
        return resourceType.equals("latest_idr_rates") ||
                resourceType.equals("historical_idr_usd") ||
                resourceType.equals("supported_currencies");
    }

    private ApiErrorResponse createErrorResponse(HttpStatus status, String message) {
        return ApiErrorResponse.builder()
                .status(status.value())
                .message(message)
                .timestamp(System.currentTimeMillis())
                .path("/api/finance/data")
                .build();
    }

    @lombok.Data
    @lombok.Builder
    public static class HealthResponse {
        private final String status;
        private final Map<String, Boolean> dataAvailability;
        private final boolean initialized;
    }
}

//package com.finance.api.controller;
//
//import com.finance.api.service.FinanceDataService;
//import com.finance.api.strategy.IDRDataFetcher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/finance/data")
//public class FinanceController {
//
//    @Autowired
//    private FinanceDataService financeDataService;
//
//    @GetMapping("/{resourceType}")
//    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
//
//        // ✅ TANPA if/else atau switch!
//        // Langsung ambil strategi dari Map berdasarkan resourceType
//        IDRDataFetcher strategy = financeDataService.getStrategy(resourceType);
//
//        // Eksekusi strategi yang dipilih
//        Object data = strategy.fetchData();
//
//        return ResponseEntity.ok(data);
//    }
//}