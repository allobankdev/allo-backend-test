package com.allobank.frankfurter_aggregator.service.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.frankfurter_aggregator.dto.CurrencyData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements DataFetcherStrategy {
    
    private final WebClient webClient;
    
    @Override
    public String getResourceType() {
        return "supported_currencies";
    }
    
    @Override
    public Mono<Object> fetchData() {
        log.info("Fetching supported currencies from external API");
        
         return webClient.get()
                .uri("/currencies")  
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    CurrencyData result = new CurrencyData();
                    
                    @SuppressWarnings("unchecked")
                    Map<String, String> currencies = (Map<String, String>) response;
                    result.setCurrencies(currencies);
                    
                    return result;
                })
                .cast(Object.class)  // Tambahkan ini untuk cast ke Object
                .doOnError(e -> log.error("Error fetching currencies: {}", e.getMessage()));
    }
}
