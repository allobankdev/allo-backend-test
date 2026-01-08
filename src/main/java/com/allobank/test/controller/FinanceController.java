package com.allobank.test.controller;

import com.allobank.test.service.strategy.IDRDataFetcher;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Dibuat oleh: Andre Rizaldi Brillianto
 * Email: andrerizaldib@gmail.com
 * Date: Wednesday, 7-January-2026
 * description: allo-bank-test
 */
// controller
@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final Map<String, IDRDataFetcher> strategies;

    public FinanceController(Map<String, IDRDataFetcher> strategies) {
        this.strategies = strategies;
    }

    // sesuai resourceType
    @GetMapping("/data/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@PathVariable String resourceType) {
        IDRDataFetcher strategy = strategies.get(resourceType);
        // jika strategy null, return not found
        if (strategy == null) {
            return ResponseEntity.notFound().build();
        }

        // mendapatkan data dari cache(memory)
        // supaya cepat, dan mengembalikan
        Object data = strategy.getCachedData();
        return ResponseEntity.ok(data);
    }
}
