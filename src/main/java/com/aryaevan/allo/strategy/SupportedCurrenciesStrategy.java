package com.aryaevan.allo.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

/**
 * Strategy implementation for fetching the list of supported currencies.
 * Handles the /currencies resource from Frankfurter API.
 */
@Component
public class SupportedCurrenciesStrategy implements IDRDataFetcher {
    
    private final WebClient webClient;
    
    @Autowired
    public SupportedCurrenciesStrategy(WebClient webClient) {
        this.webClient = webClient;
    }
    
    @Override
    public Object fetchData() {
        // Fetch the list of supported currencies
        return webClient
                .get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
    
    @Override
    public String getResourceType() {
        return "supported_currencies";
    }
}
