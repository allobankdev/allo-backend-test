package com.allobank.idr_rate_aggregator.controller;

import com.allobank.idr_rate_aggregator.service.DataCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for serving finance data endpoints.
 * 
 * This controller demonstrates:
 * - Clean separation of concerns (delegates to service layer)
 * - No conditional logic (strategy pattern handles routing)
 * - Proper error handling and logging
 * - RESTful design principles
 */
@RestController
@RequestMapping("/api/finance/data")
@Slf4j
public class FinanceDataController {

    private final DataCacheService dataCacheService;

    @Autowired
    public FinanceDataController(DataCacheService dataCacheService) {
        this.dataCacheService = dataCacheService;
    }

    /**
     * Retrieves finance data for a specific resource type.
     * 
     * Supported resource types:
     * - latest_idr_rates: Latest IDR exchange rates with USD buy spread calculation
     * - historical_idr_usd: Historical IDR to USD rates (2024-01-01 to 2024-01-05)
     * - supported_currencies: List of all supported currency codes
     * 
     * The strategy pattern ensures that the appropriate data fetcher is selected
     * without any conditional logic in the controller.
     *
     * @param resourceType the type of resource to retrieve
     * @return ResponseEntity containing the requested data
     */
    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@PathVariable String resourceType) {
        log.info("Received request for resource type: {}", resourceType);

        try {
            // DataCacheService handles strategy selection and data retrieval
            Object data = dataCacheService.getData(resourceType);
            
            log.info("Successfully retrieved data for resource type: {}", resourceType);
            return ResponseEntity.ok(data);
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid resource type requested: {}", resourceType);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                            "Invalid resource type: " + resourceType,
                            "Supported types: " + dataCacheService.getSupportedResourceTypes()
                    ));
                    
        } catch (IllegalStateException e) {
            log.error("Data not loaded for resource type: {}", resourceType);
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ErrorResponse(
                            "Data not available",
                            "The requested data has not been loaded yet. Please try again later."
                    ));
                    
        } catch (Exception e) {
            log.error("Unexpected error retrieving data for resource type: {}", resourceType, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(
                            "Internal server error",
                            "An unexpected error occurred while processing your request"
                    ));
        }
    }

//     /**
//      * Health check endpoint to verify data loading status.
//      * 
//      * @return ResponseEntity with health status
//      */
//     @GetMapping("/health")
//     public ResponseEntity<Object> healthCheck() {
//         boolean allDataLoaded = dataCacheService.getSupportedResourceTypes().stream()
//                 .allMatch(dataCacheService::isDataLoaded);

//         if (allDataLoaded) {
//             return ResponseEntity.ok(new HealthResponse(
//                     "healthy",
//                     "All data loaded successfully",
//                     dataCacheService.getSupportedResourceTypes()
//             ));
//         } else {
//             return ResponseEntity
//                     .status(HttpStatus.SERVICE_UNAVAILABLE)
//                     .body(new HealthResponse(
//                             "unhealthy",
//                             "Not all data has been loaded",
//                             dataCacheService.getSupportedResourceTypes()
//                     ));
//         }
//     }

    /**
     * Error response DTO for consistent error handling
     */
    private record ErrorResponse(String error, String message) {}

//     /**
//      * Health response DTO
//      */
//     private record HealthResponse(
//             String status,
//             String message,
//             java.util.Set<String> supportedResourceTypes
//     ) {}
}
