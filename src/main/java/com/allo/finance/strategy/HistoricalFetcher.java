package com.allo.finance.strategy;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class HistoricalFetcher implements IDRDataFetcher {

    private final WebClient client;

    public HistoricalFetcher(WebClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetch() {

        Map res = client.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Map<String, Double>> rates =
                (Map<String, Map<String, Double>>) res.get("rates");

        // ✅ FIX: convert nested map
        Map<String, Object> formattedRates = new LinkedHashMap<>();

        rates.forEach((date, currencyMap) -> {

            Map<String, Object> inner = new LinkedHashMap<>();

            currencyMap.forEach((currency, value) -> {
                BigDecimal bd = new BigDecimal(value.toString());
                inner.put(currency, bd);
            });

            formattedRates.put(date, inner);
        });

        // replace
        res.put("rates", formattedRates);

        return res;
    }
}