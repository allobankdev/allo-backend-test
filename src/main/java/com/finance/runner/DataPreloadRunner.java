package com.finance.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.finance.service.FinanceCacheService;

@Component
public class DataPreloadRunner implements ApplicationRunner{

    private final WebClient webClient;
    private final FinanceCacheService cacheService;

    public DataPreloadRunner(
        WebClient webClient,
        FinanceCacheService cacheService
    ) {
        this.webClient = webClient;
        this.cacheService = cacheService;
    }

    
    @Override
    public void run(ApplicationArguments args) {
        
        Object latest = webClient.get()
            .uri("/latest?base=IDR")
            .retrieve()
            .bodyToMono(Object.class)
            .block();

        Object historical = webClient.get()
            .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
            .retrieve()
            .bodyToMono(Object.class)
            .block();
        
        Object currencies = webClient.get()
            .uri("currencies")
            .retrieve()
            .bodyToMono(Object.class)
            .block();

        cacheService.put("latest_idr_rates", latest);
        cacheService.put("historical_idr_usd", historical);
        cacheService.put("supported_currencies", currencies);

        System.out.println("Data successfully preloaded!");

    }

}
