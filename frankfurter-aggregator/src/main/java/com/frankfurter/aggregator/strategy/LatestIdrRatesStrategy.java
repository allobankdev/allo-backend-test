package com.frankfurter.aggregator.strategy;

import com.frankfurter.aggregator.dto.internal.FinanceDataResponse;
import com.frankfurter.aggregator.service.GitHubUsernameCalculator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class LatestIdrRatesStrategy implements IDRDataFetcher {
    private final RestTemplate restTemplate;
    private final GitHubUsernameCalculator calculator;

    public LatestIdrRatesStrategy(RestTemplate restTemplate, GitHubUsernameCalculator calculator) {
        this.restTemplate = restTemplate;
        this.calculator = calculator;
    }

    @Override
    public String getResourceType() { 
        return "latest_idr_rates"; 
    }

    @Override
    public FinanceDataResponse fetchData() {
        try {
            // Call API
            String url = "https://api.frankfurter.app/latest?base=IDR";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response == null) {
                return null;
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("base", response.get("base"));
            data.put("date", response.get("date"));
            data.put("rates", response.get("rates"));
            
            if (response.get("rates") instanceof Map) {
                Map<String, Double> rates = (Map<String, Double>) response.get("rates");
                
                if (rates.containsKey("USD")) {
                    double usdRate = rates.get("USD");
                    
                    double spreadFactor = calculator.calculateSpreadFactor();
                    
                    double usdBuySpreadIdr = (1.0 / usdRate) * (1.0 + spreadFactor);
                    
                    BigDecimal formattedValue = BigDecimal.valueOf(usdBuySpreadIdr)
                        .setScale(6, RoundingMode.HALF_UP);
                    
                    // Add required fields
                    data.put("USD_BuySpread_IDR", formattedValue.doubleValue());
                    data.put("spread_factor", spreadFactor);
                    data.put("github_username", calculator.getGithubUsername());
                    
                    data.put("original_USD_rate", usdRate);
                    data.put("calculation", "(1 / " + usdRate + ") * (1 + " + spreadFactor + ")");
                }
            }
            
            return new FinanceDataResponse(getResourceType(), LocalDateTime.now(), data);
            
        } catch (Exception e) {
            System.err.println("Error fetching latest rates: " + e.getMessage());
            return null;
        }
    }
}