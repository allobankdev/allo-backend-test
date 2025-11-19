package com.allo.backend.test.code.controller;

import com.allo.backend.test.code.service.DataStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for serving financial data.
 * Data is served from in-memory storage loaded on startup.
 */
@Slf4j
@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceDataController {

    private final DataStorageService dataStorageService;

    /**
     * Retrieves financial data for the specified resource type.
     *
     * @param resourceType One of: latest_idr_rates, historical_idr_usd, supported_currencies
     * @return The financial data for the requested resource type
     */
    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@PathVariable String resourceType) {
        log.info("Received request for resource type: {}", resourceType);

        Object data = dataStorageService.getData(resourceType);

        log.debug("Returning data for resource type: {}", resourceType);
        return ResponseEntity.ok(data);
    }
}
