package com.allobank.strategy;

import com.allobank.dto.HistoricalRatesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Strategy for fetching historical IDR to USD exchange rates.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HistoricalIDRUSDStrategy implements IDRDataFetcher {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${frankfurter.api.base-url}")
    private String baseUrl;
    
    private static final String RESOURCE_TYPE = "historical_idr_usd";
    private static final String DATE_RANGE = "2024-01-01..2024-01-05";
    
    @Override
    public Object fetchData() throws Exception {
        log.info("Fetching historical IDR-USD rates for range {}", DATE_RANGE);
        
        String url = baseUrl + "/" + DATE_RANGE + "?from=IDR&to=USD";
        String response = restTemplate.getForObject(url, String.class);
        
        if (response == null) {
            throw new RuntimeException("Failed to fetch historical rates: null response");
        }
        
        Map<String, Object> data = objectMapper.readValue(response, Map.class);
        
        HistoricalRatesResponse historicalResponse = HistoricalRatesResponse.builder()
                .base((String) data.get("base"))
                .startDate(DATE_RANGE.split("\\.\\.")[0])
                .endDate(DATE_RANGE.split("\\.\\.")[1])
                .rates((Map<String, Map<String, BigDecimal>>) data.get("rates"))
                .build();
        
        log.info("Successfully fetched and transformed historical IDR-USD rates");
        return historicalResponse;
    }
    
    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }
}
