package com.hanifnfl.allobank.controller;

import com.hanifnfl.allobank.exception.DataNotAvailableException;
import com.hanifnfl.allobank.exception.ResourceTypeNotFoundException;
import com.hanifnfl.allobank.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceDataController {

    private final Map<String, IDRDataFetcher> strategies;

    @GetMapping("/{resourceType}")
    public ResponseEntity<List<?>> getFinanceData(@PathVariable String resourceType) {
        IDRDataFetcher strategy = strategies.get(resourceType);
        if (strategy == null) {
            throw new ResourceTypeNotFoundException(resourceType);
        }

        List<?> data = strategy.getCachedData();
        if (data == null || data.isEmpty()) {
            throw new DataNotAvailableException(resourceType);
        }

        return ResponseEntity.ok(data);
    }
}
