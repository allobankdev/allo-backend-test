package com.frankfurter.aggregator.controller;

import com.frankfurter.aggregator.service.DataStorageService;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {
    private final DataStorageService dataStorageService;

    public FinanceDataController(DataStorageService dataStorageService) {
        this.dataStorageService = dataStorageService;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<List<Object>> getFinanceData(@PathVariable String resourceType) {
        Object data = dataStorageService.getData(resourceType);
        
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<Object> responseArray = Collections.singletonList(data);
        return ResponseEntity.ok(responseArray);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        int dataCount = dataStorageService.getAllData().size();
        return ResponseEntity.ok("Service running. Data loaded: " + dataCount);
    }
}