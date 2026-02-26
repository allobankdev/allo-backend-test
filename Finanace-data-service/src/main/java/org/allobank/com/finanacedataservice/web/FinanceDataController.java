package org.allobank.com.finanacedataservice.web;

import org.allobank.com.finanacedataservice.service.FinanceDataCache;
import org.allobank.com.finanacedataservice.strategy.IdrDataFetcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final Map<String, IdrDataFetcher> fetcherMap;
    private final FinanceDataCache cache;


    public FinanceDataController(Map<String, IdrDataFetcher> fetcherMap, FinanceDataCache cache) {
        this.fetcherMap = fetcherMap;
        this.cache = cache;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<List<?>> getFinanceData(@PathVariable String resourceType) {
        IdrDataFetcher fetcher = fetcherMap.get(resourceType);
        if (fetcher == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown resourceType: " + resourceType);
        }
        List<?> cachedData = fetcher.getCachedData(cache);
        return ResponseEntity.ok(cachedData);
    }
}
