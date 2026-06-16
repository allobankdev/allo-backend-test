package com.example.allobank.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetch() {
        String url =
            "https://api.frankfurter.app/2024-01-01..2024-01-05?from=IDR&to=USD";

        return restTemplate.getForObject(url, Object.class);
    }
}
