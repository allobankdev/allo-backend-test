package com.allobank.idr_rate_aggregator.controller;

import com.allobank.idr_rate_aggregator.dto.ApiResponse;
import com.allobank.idr_rate_aggregator.model.FinanceData;
import com.allobank.idr_rate_aggregator.service.FinanceService;
import com.allobank.idr_rate_aggregator.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final FinanceService financeService;
    private final Map<String, IDRDataFetcher> fetcherMap;

    // Spring otomatis inject semua IDRDataFetcher bean ke dalam Map
    // key = bean name, kita override dengan getResourceType()
    public FinanceController(FinanceService financeService,
                             List<IDRDataFetcher> fetchers) {
        this.financeService = financeService;
        // Build map dari resourceType → fetcher (untuk validasi resourceType)
        this.fetcherMap = new java.util.HashMap<>();
        fetchers.forEach(f -> fetcherMap.put(f.getResourceType(), f));
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<ApiResponse<Object>> getData(
            @PathVariable String resourceType) {

        log.info("Request received for resourceType: {}", resourceType);

        // Validasi resourceType — no if/else, pakai map lookup
        if (!fetcherMap.containsKey(resourceType)) {
            log.warn("Invalid resourceType requested: {}", resourceType);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(
                            "Invalid resourceType: '" + resourceType + "'. " +
                            "Valid values: " + fetcherMap.keySet()));
        }

        return financeService.getByResourceType(resourceType)
                .map(data -> {
                    log.info("Returning data for resourceType: {}", resourceType);
                    return ResponseEntity.ok(ApiResponse.success(data.getData()));
                })
                .orElseGet(() -> {
                    log.error("Data not found in store for resourceType: {}", resourceType);
                    return ResponseEntity.internalServerError()
                            .body(ApiResponse.error(
                                    "Data not available for: " + resourceType));
                });
    }
}