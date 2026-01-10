package com.allobank.controller;

import com.allobank.dto.ApiResponse;
import com.allobank.dto.ErrorResponse;
import com.allobank.service.DataFetchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * REST Controller for the Allo Bank Finance API.
 * Exposes a single polymorphic endpoint that serves data for multiple resource types.
 * 
 * Uses the Strategy Pattern to dynamically handle different resource types without
 * conditional logic in the controller layer.
 */
@Slf4j
@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {
    
    private final DataFetchingService dataFetchingService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    /**
     * Polymorphic endpoint for retrieving data for different resource types.
     * 
     * Supported resource types:
     * - latest_idr_rates: Latest exchange rates with IDR as base
     * - historical_idr_usd: Historical IDR to USD rates for a specific date range
     * - supported_currencies: List of all supported currencies
     * 
     * @param resourceType the type of data to retrieve
     * @return ApiResponse containing the requested data or error information
     */
    @GetMapping("/data/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
        log.info("Received request for resource type: {}", resourceType);
        
        // Validate resource type
        if (resourceType == null || resourceType.trim().isEmpty()) {
            ErrorResponse error = ErrorResponse.builder()
                    .message("Resource type cannot be empty")
                    .resourceType(resourceType)
                    .timestamp(LocalDateTime.now().format(FORMATTER))
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build();
            
            log.warn("Invalid resource type: {}", resourceType);
            return ResponseEntity.badRequest().body(error);
        }
        
        try {
            // Retrieve data from the in-memory store
            Object data = dataFetchingService.getStoredData(resourceType);
            
            if (data == null) {
                ErrorResponse error = ErrorResponse.builder()
                        .message("Resource type not found: " + resourceType)
                        .resourceType(resourceType)
                        .timestamp(LocalDateTime.now().format(FORMATTER))
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .build();
                
                log.warn("Resource type not found: {}", resourceType);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            // Build successful response
            ApiResponse<?> response = ApiResponse.builder()
                    .resourceType(resourceType)
                    .data(data)
                    .timestamp(LocalDateTime.now().format(FORMATTER))
                    .isSuccess(true)
                    .build();
            
            log.info("Successfully retrieved data for resource type: {}", resourceType);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ErrorResponse error = ErrorResponse.builder()
                    .message("Error processing request: " + e.getMessage())
                    .resourceType(resourceType)
                    .timestamp(LocalDateTime.now().format(FORMATTER))
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            
            log.error("Error processing request for resource type: {}", resourceType, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Health check endpoint.
     * 
     * @return status information
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        boolean initialized = dataFetchingService.isDataStoreInitialized();
        
        ApiResponse<String> response = ApiResponse.<String>builder()
                .resourceType("health")
                .data(initialized ? "UP" : "INITIALIZING")
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .isSuccess(initialized)
                .build();
        
        HttpStatus status = initialized ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(status).body(response);
    }
}
