package com.allobank.frankfurter_aggregator.service.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.frankfurter_aggregator.config.AppProperties;
import com.allobank.frankfurter_aggregator.dto.LatestRatesResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatestIdrRatesFetcher implements DataFetcherStrategy {
    
    private final WebClient webClient;
    private final AppProperties appProperties;
    
    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }
    
    @Override
    public Mono<Object> fetchData() {
        log.info("Fetching latest IDR rates from external API");
        
        return webClient.get()
                .uri("/latest?base=IDR")  
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::transformResponse)
                .cast(Object.class)  
                .doOnError(e -> log.error("Error fetching latest rates: {}", e.getMessage()));
    }
    
    private LatestRatesResponse transformResponse(Map<String, Object> response) {
        LatestRatesResponse result = new LatestRatesResponse();
        result.setBase((String) response.get("base"));  
        result.setDate((String) response.get("date"));  
        
        @SuppressWarnings("unchecked")
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");
        result.setRates(rates);
        
        if (rates != null && rates.containsKey("USD")) {
            double rateUsd = rates.get("USD");  
            double spreadFactor = calculateSpreadFactor();
            double usdBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);
            result.setUsdBuySpreadIdr(usdBuySpreadIdr);
            
            log.info("Calculated USD_BuySpread_IDR: {} with spread factor: {}", 
                    usdBuySpreadIdr, spreadFactor);
        }
        
        return result;
    }
    
    private double calculateSpreadFactor() {
        String username = appProperties.getGithub().getUsername().toLowerCase();
        int unicodeSum = username.chars().sum();
        double spreadFactor = (unicodeSum % 1000) / 100000.0;
        
        log.info("Username: {}, Unicode sum: {}, Spread factor: {}", 
                username, unicodeSum, spreadFactor);
        
        return spreadFactor;
    }
}
