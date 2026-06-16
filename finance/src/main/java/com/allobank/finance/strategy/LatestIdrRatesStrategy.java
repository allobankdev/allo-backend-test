package com.allobank.finance.strategy;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatestIdrRatesStrategy implements IDRDataFetcher {

    private final FrankfurterClient client;
    private final AppProperties appProperties;

    @Override
    public Object fetch() {
        log.info("Fetching latest IDR rates");

        Map<String, Object> response = client.getLatestIdrRates();
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");

        double rateUsd = rates.get("USD");
        double spreadFactor = calculateSpreadFactor();
        double usdBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);

        Map<String, Object> result = new HashMap<>(response);
        result.put("USD_BuySpread_IDR", usdBuySpreadIdr);
        result.put("spreadFactor", spreadFactor);

        return result;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    private double calculateSpreadFactor() {
        String username = appProperties.getGithubUsername().toLowerCase();
        int sum = username.chars().sum();
        return (sum % 1000) / 100000.0;
    }
}
