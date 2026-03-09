package com.aryaevan.allo.strategy;

import com.aryaevan.allo.dto.LatestRatesResponse;
import com.aryaevan.allo.util.SpreadCalculationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.HashMap;
import java.util.Map;

/**
 * Strategy implementation for fetching the latest IDR exchange rates.
 * Handles the /latest?base=IDR resource from Frankfurter API.
 * Calculates and includes USD_BuySpread_IDR field based on spread factor.
 */
@Component
public class LatestIDRRatesStrategy implements IDRDataFetcher {
    
    private final WebClient webClient;
    
    @Value("${app.github-username}")
    private String githubUsername;
    
    @Autowired
    public LatestIDRRatesStrategy(WebClient webClient) {
        this.webClient = webClient;
    }
    
    @Override
    public Object fetchData() {
        // Fetch latest rates with IDR as base
        LatestRatesResponse response = webClient
                .get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .block();
        
        if (response == null || response.getRates() == null) {
            return new HashMap<>();
        }
        
        // Create a new map with the spread calculation
        Map<String, Object> result = new HashMap<>();
        result.put("base", response.getBase());
        result.put("date", response.getDate());
        
        // Copy all original rates
        Map<String, Double> enrichedRates = new HashMap<>(response.getRates());
        
        // Calculate and add USD_BuySpread_IDR
        Double usdRate = response.getRates().get("USD");
        if (usdRate != null) {
            double spreadFactor = SpreadCalculationUtil.calculateSpreadFactor(githubUsername);
            double usdBuySpreadIdR = SpreadCalculationUtil.calculateUsdBuySpreadIdR(usdRate, spreadFactor);
            enrichedRates.put("USD_BuySpread_IDR", usdBuySpreadIdR);
        }
        
        result.put("rates", enrichedRates);
        
        return result;
    }
    
    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }
}

