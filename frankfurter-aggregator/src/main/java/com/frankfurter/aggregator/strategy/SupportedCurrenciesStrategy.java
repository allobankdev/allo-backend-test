package com.frankfurter.aggregator.strategy;

import com.frankfurter.aggregator.dto.internal.FinanceDataResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class SupportedCurrenciesStrategy implements IDRDataFetcher {
    private final RestTemplate restTemplate;

    public SupportedCurrenciesStrategy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getResourceType() { 
        return "supported_currencies"; 
    }

    @Override
    public FinanceDataResponse fetchData() {
        try {
            String url = "https://api.frankfurter.app/currencies";
            Map<String, String> response = restTemplate.getForObject(url, Map.class);
            
            Map<String, Object> data = new HashMap<>();
            
            if (response != null) {
                // TRANSFORM MAP TO ARRAY as required
                List<Map<String, String>> currenciesArray = new ArrayList<>();
                response.forEach((code, name) -> {
                    Map<String, String> currency = new HashMap<>();
                    currency.put("code", code);
                    currency.put("name", name);
                    currenciesArray.add(currency);
                });
                
                data.put("currencies", currenciesArray);  // ← ARRAY not Map
                data.put("total_currencies", currenciesArray.size());
            } else {
                data.put("currencies", new ArrayList<>());
                data.put("total_currencies", 0);
            }
            
            return new FinanceDataResponse(getResourceType(), LocalDateTime.now(), data);
            
        } catch (Exception e) {
            System.err.println("Error fetching currencies: " + e.getMessage());
            return null;
        }
    }
}