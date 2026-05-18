package com.allobank.strategy;

import com.allobank.dto.CurrenciesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Strategy for fetching the list of all supported currencies
 * from the Frankfurter API.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IDRDataFetcher {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${frankfurter.api.base-url}")
    private String baseUrl;
    
    private static final String RESOURCE_TYPE = "supported_currencies";
    
    @Override
    public Object fetchData() throws Exception {
        log.info("Fetching supported currencies from Frankfurter API");
        
        String url = baseUrl + "/currencies";
        String response = restTemplate.getForObject(url, String.class);
        
        if (response == null) {
            throw new RuntimeException("Failed to fetch currencies: null response");
        }
        
        @SuppressWarnings("unchecked")
        Map<String, String> currencies = objectMapper.readValue(response, Map.class);
        
        CurrenciesResponse currenciesResponse = CurrenciesResponse.builder()
                .currencies(currencies)
                .build();
        
        log.info("Successfully fetched {} supported currencies", currencies.size());
        return currenciesResponse;
    }
    
    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }
}
