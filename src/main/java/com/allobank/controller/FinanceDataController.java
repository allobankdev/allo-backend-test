package com.allobank.controller;

import com.allobank.dto.ApiErrorResponse;
import com.allobank.service.InMemoryDataStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * REST Controller for finance data aggregation endpoint.
 * Uses map-based data retrieval from in-memory store (data pre-loaded by ApplicationRunner).
 * Strategy selection is handled by Spring's dependency injection of all IDRDataFetcher implementations.
 */
@Slf4j
@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceDataController {

    private static final Set<String> VALID_RESOURCE_TYPES = Set.of(
            "latest_idr_rates",
            "historical_idr_usd",
            "supported_currencies"
    );

    private final InMemoryDataStore dataStore;

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
        log.debug("Received request for resource type: {}", resourceType);

        // Validate resource type using set-based lookup (no if/else chain)
        if (!VALID_RESOURCE_TYPES.contains(resourceType)) {
            ApiErrorResponse error = ApiErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error("Invalid Resource Type")
                    .message("Resource type must be one of: " + String.join(", ", VALID_RESOURCE_TYPES))
                    .path("/api/finance/data/" + resourceType)
                    .build();
            return ResponseEntity.badRequest().body(error);
        }

        // Check if data is loaded
        if (!dataStore.isDataLoaded()) {
            ApiErrorResponse error = ApiErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                    .error("Data Not Ready")
                    .message("Data is still being loaded. Please try again in a moment.")
                    .path("/api/finance/data/" + resourceType)
                    .build();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }

        // Retrieve data from in-memory store (map-based lookup)
        Object data = dataStore.getData(resourceType);
        
        if (data == null) {
            ApiErrorResponse error = ApiErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.NOT_FOUND.value())
                    .error("Data Not Found")
                    .message("No data available for resource type: " + resourceType)
                    .path("/api/finance/data/" + resourceType)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        // Return data as array (wrapping single object in array for unified response)
        List<Object> response = List.of(data);
        return ResponseEntity.ok(response);
    }
}

