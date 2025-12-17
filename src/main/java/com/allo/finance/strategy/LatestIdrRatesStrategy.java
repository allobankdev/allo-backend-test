package com.allo.finance.strategy;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

public class LatestIdrRatesStrategy implements IDRDataFetcher {

    private final WebClient client;
    private static final String USERNAME = "nurulhadi24";

    public LatestIdrRatesStrategy(WebClient client) {
        this.client = client;
    }

    @Override
    public String resourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetch() {
        Map<String, Object> response = client.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Double> rates = (Map<String, Double>) response.get("rates");
        double usdRate = rates.get("USD");

        double spread = calculateSpread(USERNAME);
        double buySpread = (1 / usdRate) * (1 + spread);

        response.put("USD_BuySpread_IDR", buySpread);
        return response;
    }

    private double calculateSpread(String username) {
        int sum = username.toLowerCase()
                .chars()
                .sum();
        return (sum % 1000) / 100000.0;
    }
}

@Override
public Object fetch() {

    Map<String, Object> response =
            client.get()
                    .uri("/latest?base=IDR")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

    Map<String, Double> rates =
            (Map<String, Double>) response.get("rates");

    double usdRate = rates.get("USD");
    double spreadFactor = calculateSpreadFactor();
    double usdBuySpread = (1 / usdRate) * (1 + spreadFactor);

    return new LatestRateResponse(
            "IDR",
            rates.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> BigDecimal.valueOf(e.getValue())
                    )),
            BigDecimal.valueOf(usdBuySpread)
    );
}

