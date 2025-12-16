package com.allo.backend.strategy;

import com.allo.backend.model.LatestIdrRatesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {
    private final WebClient webClient;
    private final double spreadFactor;

    public LatestIdrRatesFetcher(WebClient webClient, @Value("${github.username}") String githubUsername) {
        this.webClient = webClient;
        this.spreadFactor = calculateSpreadFactor(githubUsername);
    }

    @Override
    public Object fetchData() {
        Map<String, Object> response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        String base = (String) response.get("base");
        String date = (String) response.get("date");
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");
        Double rateUsd = rates.get("USD");
        Double usdBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);
        return new LatestIdrRatesResponse(base, date, rates, usdBuySpreadIdr);
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    private double calculateSpreadFactor(String username) {
        int sum = username.toLowerCase().chars().sum();
        return (sum % 1000) / 100000.0;
    }
}
