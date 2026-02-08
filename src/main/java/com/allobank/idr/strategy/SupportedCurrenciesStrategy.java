package com.allobank.idr.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IDRDataFetcher {
    
    private final WebClient webClient;

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Map<String, Object> fetchData() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
