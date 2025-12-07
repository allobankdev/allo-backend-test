package com.example.allo_bank.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class LatestIdrRate extends BaseDataFetcher{

    @Value("${frankfurter.api.base-url}")
    private String baseUrl;

    @Value("${github.username}")
    private String githubUsername;

    public LatestIdrRate(
            RestTemplate restTemplate,
            @Value("${frankfurter.api.base-url}") String baseUrl
    ) {
        super(restTemplate, baseUrl);
    }

    @Override
    public String getResourceName() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchData() {
        String url = baseUrl + "/latest?base=IDR";
        String username = githubUsername.toLowerCase();

        Map response = getRestTemplate().getForObject(url, Map.class);

        Map<String, Double> rates = (Map<String, Double>) response.get("rates");
        double usdRate = rates.get("USD");

        int totalUnicode = username.chars().sum();
        double spreadFactor = (totalUnicode / 1000) / 100000;
        double usdBuySpread = (1 / usdRate) * (1 + spreadFactor);

        response.put("usdBuySpreadIdr", usdBuySpread);

        return response;

    }
}
