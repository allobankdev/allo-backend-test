package com.allobank.idr.controller;

import com.allobank.idr.service.DataStoreService;
import com.allobank.idr.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceDataController {
    
    private final Map<String, IDRDataFetcher> dataFetcherMap;
    private final DataStoreService dataStoreService;

    @GetMapping("/{resourceType}")
    public ResponseEntity<Map<String, Object>> getData(@PathVariable String resourceType) {
        if (!dataFetcherMap.containsKey(resourceType)) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Invalid resource type",
                "validTypes", dataFetcherMap.keySet()
            ));
        }

        Map<String, Object> data = dataStoreService.getData(resourceType);
        
        if (data == null) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Data not available"
            ));
        }

        return ResponseEntity.ok(data);
    }
}
