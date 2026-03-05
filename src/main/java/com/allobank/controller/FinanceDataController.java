package com.allobank.controller;

import com.allobank.services.DataCacheService;
import com.allobank.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
@Slf4j
public class FinanceDataController {

    private final Map<String, IDRDataFetcher> fetchers;
    private final DataCacheService cache;

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getData(
            @PathVariable String resourceType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        
        log.debug("Request: {} with params: startDate={}, endDate={}, from={}, to={}", 
                resourceType, startDate, endDate, from, to);

        
        if (!fetchers.containsKey(resourceType)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Unknown resource: " + resourceType,
                               "available", fetchers.keySet()));
        }

        
        if (!cache.isReady()) {
            return ResponseEntity.status(503)
                    .body(Map.of("error", "Service not ready yet"));
        }

        if ("historical_idr_usd".equals(resourceType)) {
            IDRDataFetcher fetcher = fetchers.get(resourceType);
            
            
            Map<String, String> params = new HashMap<>();
            if (startDate != null) params.put("startDate", startDate);
            if (endDate != null) params.put("endDate", endDate);
            if (from != null) params.put("from", from);
            if (to != null) params.put("to", to);
            
            Object data = fetcher.fetchData(params);
            return ResponseEntity.ok(data);
        }

        Object data = cache.get(resourceType);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "status", cache.isReady() ? "ready" : "loading",
                "resources", fetchers.keySet()
        ));
    }
}