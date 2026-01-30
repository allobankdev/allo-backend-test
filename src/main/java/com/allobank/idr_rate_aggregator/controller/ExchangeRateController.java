package com.allobank.idr_rate_aggregator.controller;

import com.allobank.idr_rate_aggregator.strategy.DataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final Map<String, DataFetcher> strategies;

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {

        DataFetcher strategy = strategies.get(resourceType);

        if (strategy == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Resource tidak ditemukan. Gunakan: latest_idr_rates, historical_idr_usd, atau supported_currencies"));
        }

        List<?> data = strategy.fetchData();
        if (data == null || data.isEmpty()) {
            throw new RuntimeException("Data belum tersedia atau Gagal Fetch dari API External");
        }

        return ResponseEntity.ok(data);
    }
}
