package com.allo.finance.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class LatestFetcher implements IDRDataFetcher {

    private final WebClient client;

    @Value("${app.github-username}")
    private String username;

    public LatestFetcher(WebClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetch() {

        Map res = client.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Double> rates = (Map<String, Double>) res.get("rates");

        Map<String, Object> formattedRates = new LinkedHashMap<>();
        rates.forEach((k, v) -> {
            BigDecimal bd = new BigDecimal(v.toString());
            formattedRates.put(k, bd);
        });

        res.put("rates", formattedRates);

        double usd = rates.get("USD");

        int sum = username.chars().sum();
        double spread = (sum % 1000) / 100000.0;

        double calc = (1 / usd) * (1 + spread);

        BigDecimal spreadResult = new BigDecimal(String.valueOf(calc));

        res.put("USD_BuySpread_IDR", spreadResult);

        return res;
    }
}