
package com.allo_backend_test.finance.adapter;

import com.allo_backend_test.finance.Utils.Const;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;

    @Value("${frankfurter.github-username}")
    private String githubUsername;

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchAndTransform() {
        Map<String, Object> response =
                restTemplate.getForObject("/latest?base=IDR", Map.class);

        Map<String, Double> rates =
                (Map<String, Double>) response.get("rates");

        Double rateUsd = rates.get(Const.USD);

        double spreadFactor = calculateSpreadFactor(githubUsername);
        double usdBuySpread = (1 / rateUsd) * (1 + spreadFactor);

        response.put("USD_BuySpread_IDR", usdBuySpread);
        return response;
    }

    private double calculateSpreadFactor(String username) {
        int sum = username.toLowerCase().chars().sum();
        return (sum % 1000) / 100000.0;
    }
}
