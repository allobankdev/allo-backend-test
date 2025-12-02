package com.allobanktest.idr.controller;

import com.allobanktest.idr.service.FinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> get(@PathVariable String resourceType) {
        try {
            Map<String, Object> payload = financeService.getData(resourceType);
            if (payload == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(payload);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(503).body(Map.of("error", ex.getMessage()));
        }
    }
}

