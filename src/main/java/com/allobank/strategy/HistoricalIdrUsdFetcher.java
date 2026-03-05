package com.allobank.strategy;

import com.allobank.config.FrankfurterClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("historical_idr_usd")
@Slf4j
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    
    @Value("${frankfurter.historical.start:2024-01-01}")
    private String defaultStartDate;
    
    @Value("${frankfurter.historical.end:2024-01-05}")
    private String defaultEndDate;
    
    @Value("${frankfurter.historical.from:IDR}")
    private String defaultFromCurrency;
    
    @Value("${frankfurter.historical.to:USD}")
    private String defaultToCurrency;

    public HistoricalIdrUsdFetcher(FrankfurterClientFactory clientFactory) {
        this.restTemplate = clientFactory.getObject();
        this.baseUrl = clientFactory.getBaseUrl();
    }

    @Override
    public Object fetchData() {
        return fetchWithParams(Map.of());
    }

    @Override
    public Object fetchData(Map<String, String> params) {
        return fetchWithParams(params);
    }

    private Object fetchWithParams(Map<String, String> params) {
        
        String startDate = params.getOrDefault("startDate", defaultStartDate);
        String endDate = params.getOrDefault("endDate", defaultEndDate);
        String fromCurrency = params.getOrDefault("from", defaultFromCurrency);
        String toCurrency = params.getOrDefault("to", defaultToCurrency);
        
        log.info("Fetching historical: {} to {} ({} → {})", 
                startDate, endDate, fromCurrency, toCurrency);
        
        String url = String.format("%s/%s..%s?from=%s&to=%s",
                baseUrl, startDate, endDate, fromCurrency, toCurrency);
        
        log.debug("URL: {}", url);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        if (response == null) {
            throw new RuntimeException("Failed to fetch historical rates");
        }
        
        return transformToList(response);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> transformToList(Map<String, Object> response) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        Map<String, Map<String, Object>> rates = 
            (Map<String, Map<String, Object>>) response.get("rates");
        
        if (rates != null) {
            for (Map.Entry<String, Map<String, Object>> entry : rates.entrySet()) {
                Map<String, Object> item = new java.util.HashMap<>();
                item.put("date", entry.getKey());
                item.put("IDR_to_USD", entry.getValue().get("USD"));
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }
}