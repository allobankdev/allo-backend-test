package com.allobank.allobackendtest.strategy;

import com.allobank.allobackendtest.dto.CurrenciesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class SupportedCurrenciesFetcher implements IdrDataFetcher {

    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String resourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetchFromApi() {
        @SuppressWarnings("unchecked")
        Map<String, String> currencies = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return new CurrenciesResponse(
                resourceType(),
                currencies != null ? currencies : Map.of()
        );
    }
}
