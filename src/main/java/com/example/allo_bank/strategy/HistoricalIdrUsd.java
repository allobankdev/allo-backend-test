package com.example.allo_bank.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HistoricalIdrUsd extends BaseDataFetcher{

    @Value("${frankfurter.api.base-url}")
    private String baseUrl;

//    public HistoricalIdrUsd(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    public HistoricalIdrUsd(
            RestTemplate restTemplate,
            @Value("${frankfurter.api.base-url}") String baseUrl
    ) {
        super(restTemplate, baseUrl);
    }

    @Override
    public String getResourceName() {
        return"historical_idr_usd";
    }

    @Override
    public Object fetchData() {
        String url =  baseUrl + "/2024-01-01..2024-01-05?from=IDR&to=USD";
        return restTemplate.getForObject(url, Object.class);
    }
}
