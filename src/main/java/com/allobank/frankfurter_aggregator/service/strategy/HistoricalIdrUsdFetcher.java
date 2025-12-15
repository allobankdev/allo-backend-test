package com.allobank.frankfurter_aggregator.service.strategy;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.frankfurter_aggregator.dto.HistoricalData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoricalIdrUsdFetcher implements DataFetcherStrategy {
    
    private final WebClient webClient;
    
    @Override
    public String getResourceType() {
        return "historical_idr_usd";  
    }
    
    @Override
    public Mono<Object> fetchData() {
        log.info("Fetching historical IDR-USD rates from external API");
        
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")  // Perbaikan URL
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::transformResponse)
                .cast(Object.class)  // Tambahkan ini untuk cast ke Object
                .doOnError(e -> log.error("Error fetching historical data: {}", e.getMessage()));
    }
    
    private HistoricalData transformResponse(Map<String, Object> response) {
        HistoricalData result = new HistoricalData();
        result.setFrom((String) response.get("base"));
        result.setTo("USD");  
        result.setStartDate(LocalDate.parse((String) response.get("start_date")));
        result.setEndDate(LocalDate.parse((String) response.get("end_date")));
        
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Double>> rates = (Map<String, Map<String, Double>>) response.get("rates");
        
        if (rates != null) {
            Map<LocalDate, Double> usdRates = new java.util.HashMap<>();
            rates.forEach((date, currencyRates) -> {
                if (currencyRates.containsKey("USD")) {
                    usdRates.put(LocalDate.parse(date), currencyRates.get("USD"));
                }
            });
            result.setRates(usdRates);
        }
        
        return result;
    }
}
