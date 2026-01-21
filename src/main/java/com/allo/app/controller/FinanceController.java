package com.allo.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allo.app.service.impl.HistoricalIdrUsd;
import com.allo.app.service.impl.LastestIdrRatesServiceImpl;
import com.allo.app.service.impl.SupportedCurrencies;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final HistoricalIdrUsd historicalIdrUsd;
    private final LastestIdrRatesServiceImpl latestIdrRatesService;
    private final SupportedCurrencies supportedCurrencies;

    @GetMapping("data/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@RequestParam String resourceType) {
        switch (resourceType) {
            case "latest_idr_rates":
                return ResponseEntity.ok(latestIdrRatesService.getData());
            case "historical_idr_usd":
                return ResponseEntity.ok(historicalIdrUsd.getData());
            case "supported_currencies":
                return ResponseEntity.ok(supportedCurrencies.getData());
            default:
                return ResponseEntity.badRequest().body("Invalid resource type");
        }
    }
    
}
