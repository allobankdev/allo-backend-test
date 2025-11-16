package com.allobank.controller;

import com.allobank.service.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
@Slf4j
public class FinanceDataController {

    private final Map<String, IDRDataFetcher> strategies;

    /**
     * Retrieves data for the specified resource type
     *
     * @param resourceType One of: latest_idr_rates, historical_idr_usd, supported_currencies
     * @return The cached data for the requested resource
     */
    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> getFinanceData(
            @PathVariable String resourceType) {

        log.info("Received request for resource type: {}", resourceType);

        try {
            IDRDataFetcher strategy = Optional.ofNullable(strategies.get(resourceType))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid resource type: " + resourceType +
                                                                    ". Valid types: latest_idr_rates, historical_idr_usd, supported_currencies"));
            Object data = strategy.getData();
            log.info("Successfully served data for: {}", resourceType);
            return ResponseEntity.ok(data);

        } catch (IllegalArgumentException e) {
            log.warn("Invalid resource type requested: {}", resourceType);
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        } catch (IllegalStateException e) {
            log.error("Data store not initialized", e);
            return ResponseEntity.status(503)
                    .body(new ErrorResponse("Service initializing, please try again"));
        } catch (Exception e) {
            log.error("Error serving data for: {}", resourceType, e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Internal server error"));
        }
    }


    // Response DTOs
    record ErrorResponse(String error) {
    }
}
