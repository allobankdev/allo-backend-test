package com.allobank.finance.strategy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.allobank.finance.client.FrankfurterClient;

@Component
public class LatestIdrRatesStrategy implements IDRDataFetcher {

    private final FrankfurterClient client;

    public LatestIdrRatesStrategy(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public Object fetch() {
        System.out.println(">> fetching latest IDR : ");
        
        Map<String, Object> response = client.getLatestIdrRates();
        System.out.println(">> API Response : " + response);

        Map<String, Double> rates = (Map<String, Double>) response.get("rates");

        double rateUsd = rates.get("USD");

        double spreadFactor = calculateSpreadFactor();
        double usdBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);

        Map<String, Object> result = new HashMap<>(response);
        result.put("USD_BuySpread_IDR", usdBuySpreadIdr);

        return result;
    }

    @Override
    public String getResourceType() {

        return "latest_idr_rates";
    }

    private double calculateSpreadFactor() {
        String username = "hizkiarenat";
        int sum = username.chars().sum();
        return (sum % 1000) / 100000.0;
    }

}
