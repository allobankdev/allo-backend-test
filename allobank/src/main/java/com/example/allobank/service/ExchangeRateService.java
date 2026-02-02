package com.example.allobank.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${app.github.username}")
    private String githubUsername;

    public Object getData(String resourceType) {

    	if ("latest_idr_rates".equals(resourceType)) {
    	    String url = "https://api.frankfurter.app/latest?base=IDR";

    	    Map<String, Object> response =
    	            restTemplate.getForObject(url, Map.class);

    	    Map<String, Object> rates =
    	            (Map<String, Object>) response.get("rates");

    	    Double usdRate = (Double) rates.get("USD");

    	    double spreadFactor = calculateSpreadFactor();
    	    double usdBuySpreadIdr = round((1 / usdRate) * (1 + spreadFactor));

    	    response.put("USD_BuySpread_IDR", usdBuySpreadIdr);

    	    return response;
    	}

        if ("historical_idr_usd".equals(resourceType)) {
            String url = "https://api.frankfurter.app/2024-01-01..2024-01-05?from=IDR&to=USD";
            return restTemplate.getForObject(url, Object.class);
        }
        
        if ("supported_currencies".equals(resourceType)) {
            String url = "https://api.frankfurter.app/currencies";
            return restTemplate.getForObject(url, Object.class);
        }

        return "Resource not supported yet";
    }
    
    private double calculateSpreadFactor() {
        githubUsername = githubUsername.toLowerCase();

        int sum = 0;
        for (char c : githubUsername.toCharArray()) {
            sum += (int) c;
        }
        
        return (sum % 1000) / 100000.0;
    }
    
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}