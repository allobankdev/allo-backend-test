package org.imam.allo.client;

import lombok.extern.slf4j.Slf4j;
import org.imam.allo.dto.CurrenciesResponse;
import org.imam.allo.dto.HistoricalResponse;
import org.imam.allo.dto.LatestRatesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Component
public class FrankfurterClient {
    private final WebClient webClient;

    public FrankfurterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    private <T> T execute(WebClient.RequestHeadersSpec<?> request, Class<T> clazz, String endpointName) {
        try {
            return request
                    .retrieve()
                    .bodyToMono(clazz)
                    .block();

        } catch (WebClientResponseException e) {
            log.error("API error on {}: status={}, body={}",
                    endpointName,
                    e.getStatusCode(),
                    e.getResponseBodyAsString());
            throw e;
        }
    }

    public LatestRatesResponse getLatestRates() {
        return execute(
                webClient.get().uri("/latest?base=IDR"),
                LatestRatesResponse.class,
                "latest rates"
        );
    }

    public HistoricalResponse getHistorical() {
        return execute(
                webClient.get().uri("/2024-01-01..2024-01-05?from=IDR&to=USD"),
                HistoricalResponse.class,
                "historical rates"
        );

    }

    public CurrenciesResponse getCurrencies() {
        return execute(
                webClient.get().uri("/currencies"),
                CurrenciesResponse.class,
                "currencies"
        );

    }
}
