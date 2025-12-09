package com.example.idr.rate.aggregator.controller;

import com.example.idr.rate.aggregator.fetcher.IdrDataFetcher;
import com.example.idr.rate.aggregator.store.ImmutableDataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
public class IdrRateController {

    private final ImmutableDataStore store;
    private final Map<String, IdrDataFetcher> fetcherMap;

    public IdrRateController(ImmutableDataStore store, Map<String, IdrDataFetcher> fetcherMap) {
        this.store = store;
        this.fetcherMap = fetcherMap;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getData(@PathVariable String resourceType) {
        Object data = store.get(resourceType);
        if (data == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "unknown resourceType", "allowed", fetcherMap.keySet()));
        }
        return ResponseEntity.ok(data);
    }
}
