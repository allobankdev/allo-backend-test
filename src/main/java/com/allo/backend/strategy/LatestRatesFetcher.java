package com.allo.backend.strategy;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component

public class LatestRatesFetcher implements IDRDataFetcher {
    private final WebClient webClient;

    public LatestRatesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchData() {
        var response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Double usdRate = (Double) ((Map) response.get("rates")).get("USD");

        double spreadFactor = calculateSpread("cahyamaullna");

        double result = (1 / usdRate) * (1 + spreadFactor);

        response.put("USD_BuySpread_IDR", result);

        System.out.println("res" + response);

        return response;
    }

    private double calculateSpread(String username) {
        int sum = username.chars().sum();
        return (sum % 1000) / 100000.0;
    }

}
