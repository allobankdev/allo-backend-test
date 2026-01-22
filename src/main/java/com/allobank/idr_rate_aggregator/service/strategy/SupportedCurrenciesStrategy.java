package com.allobank.idr_rate_aggregator.service.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IDRDataFetcherStrategy {

    private final WebClient webClient;

    @Override
    public Object fetchData() {
        log.info("Fetching supported currencies from Frankfurter API");
        
        try {
            Map<String, String> response = webClient.get()
                    .uri("/currencies")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            if (response == null) {
                throw new RuntimeException("Received null response from Frankfurter API");
            }
            
            List<String> currencies = List.copyOf(response.keySet());
            log.info("Successfully fetched {} supported currencies", currencies.size());
            
            return currencies;
            
        } catch (Exception e) {
            log.error("Error fetching supported currencies", e);
            throw new RuntimeException("Failed to fetch supported currencies: " + e.getMessage(), e);
        }
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }
}

