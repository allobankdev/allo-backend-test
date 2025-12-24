package com.allo.finance.strategy.impl;

import com.allo.finance.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final WebClient frankfurterWebClient;

    private static final double SPREAD_FACTOR = 0.00090;

    private double calculateUsdBuySpread(double rateUsd) {
        return (1 / rateUsd) * (1 + SPREAD_FACTOR);
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchData() {
        Map<String, Object> response = frankfurterWebClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("rates")) {
            return response;
        }

        Map<String, Object> rates = (Map<String, Object>) response.get("rates");

        if (!rates.containsKey("USD")) {
            return response;
        }

        double rateUsd = ((Number) rates.get("USD")).doubleValue();

        Map<String, Object> result = new java.util.HashMap<>(response);
        result.put("USD_BuySpread_IDR", calculateUsdBuySpread(rateUsd));

        return result;
    }

}