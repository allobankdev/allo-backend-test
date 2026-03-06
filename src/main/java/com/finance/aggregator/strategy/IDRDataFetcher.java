package com.finance.aggregator.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

public interface IDRDataFetcher {
    String getResourceType();
    Object fetchData(WebClient client);
}

@Component
class LatestRatesStrategy implements IDRDataFetcher {
    @Override public String getResourceType() { return "latest_idr_rates"; }
    
    @Override
    public Object fetchData(WebClient client) {
        Map<String, Object> res = client.get().uri("/latest?base=IDR").retrieve().bodyToMono(Map.class).block();
        Map<String, Double> rates = (Map<String, Double>) res.get("rates");
        
        // Perhitungan Spread Factor (Username: johndoe47)
        double rateUsd = rates.get("USD");
        double spreadFactor = 0.00854; 
        double buySpread = (1 / rateUsd) * (1 + spreadFactor);
        
        res.put("USD_BuySpread_IDR", buySpread);
        return res;
    }
}

@Component
class HistoricalStrategy implements IDRDataFetcher {
    @Override public String getResourceType() { return "historical_idr_usd"; }
    @Override
    public Object fetchData(WebClient client) {
        return client.get().uri("/2024-01-01..2024-01-05?from=IDR&to=USD").retrieve().bodyToMono(Object.class).block();
    }
}

@Component
class CurrenciesStrategy implements IDRDataFetcher {
    @Override public String getResourceType() { return "supported_currencies"; }
    @Override
    public Object fetchData(WebClient client) {
        Map<String, String> res = client.get().uri("/currencies").retrieve().bodyToMono(Map.class).block();
        return res.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).toList();
    }
}