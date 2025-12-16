package com.allo.backend.controller;

import com.allo.backend.service.FinanceDataStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {
    private final FinanceDataStoreService dataStoreService;

    public FinanceController(FinanceDataStoreService dataStoreService) {
        this.dataStoreService = dataStoreService;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getData(@PathVariable String resourceType) {
        Object data = dataStoreService.getData(resourceType);
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }
}
