package com.example.finance.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;

    @Autowired
    public HistoricalIdrUsdFetcher(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public List<Map<String, Object>> fetchData() {
        String url = "https://api.frankfurter.app/2024-01-01..2024-01-05?from=IDR&to=USD";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        List<Map<String, Object>> result = new ArrayList<>();
        if (response != null) {
            Object ratesObj = response.get("rates");
            if (ratesObj instanceof Map) {
                Map<String, Map<String, Double>> ratesByDate = (Map<String, Map<String, Double>>) ratesObj;
                for (Map.Entry<String, Map<String, Double>> entry : ratesByDate.entrySet()) {
                    String date = entry.getKey();
                    Map<String, Double> inner = entry.getValue();
                    Double usdRate = inner.get("USD");
                    result.add(Map.of("date", date, "rate", usdRate));
                }
            }
        }
        return result;
    }
}