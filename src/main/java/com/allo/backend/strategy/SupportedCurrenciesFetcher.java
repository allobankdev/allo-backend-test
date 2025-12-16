package com.allo.backend.strategy;

import com.allo.backend.model.SupportedCurrenciesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {
    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Object fetchData() {
        Map<String, String> response = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        return new SupportedCurrenciesResponse(response);
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }
}
