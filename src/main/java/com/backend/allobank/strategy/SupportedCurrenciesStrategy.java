package com.backend.allobank.strategy;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@Profile("!test")
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    public SupportedCurrenciesStrategy(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetchAndTransform() {

        Map<String, String> currencies = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (currencies == null) {
            throw new IllegalStateException("Empty response from Frankfurter currencies API");
        }

        return currencies;
    }
}
