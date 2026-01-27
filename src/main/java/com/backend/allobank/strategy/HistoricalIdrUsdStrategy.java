package com.backend.allobank.strategy;

import com.backend.allobank.dto.FrankfurterRatesResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
@Profile("!test")
public class HistoricalIdrUsdStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    public HistoricalIdrUsdStrategy(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetchAndTransform() {

        FrankfurterRatesResponse response = webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(FrankfurterRatesResponse.class)
                .block();

        if (response == null || response.rates() == null) {
            throw new IllegalStateException("Empty response from Frankfurter historical API");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("base", "IDR");
        result.put("rates", response.rates());

        return result;
    }
}
