package com.allobank.allobank_api.strategy.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.allobank.allobank_api.client.frankfurter.FrankfurterClient;
import com.allobank.allobank_api.dto.oas.LatestRatesOas;
import com.allobank.allobank_api.strategy.IDRDataFetcher;

public class LatestIdrRatesImpl implements IDRDataFetcher<LatestRatesOas.Response> {

    private final FrankfurterClient client;

    @Value("${app.github.username}")
    private String username;

    public LatestIdrRatesImpl(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public LatestRatesOas.Response fetchAndTransform() {
        Map<String, Object> response = client.getLatestRates();

        String base = (String) response.get("base");
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");

        double usdRate = rates.get("USD");

        double spread = calculateSpread(username);
        double usdBuySpread = (1 / usdRate) * (1 + spread);

        return new LatestRatesOas.Response(base, rates, usdBuySpread);
    }

    private double calculateSpread(String username) {
        int sum = username.toLowerCase().chars().sum();
        return (sum % 1000) / 100000.0;
    }
    
}
