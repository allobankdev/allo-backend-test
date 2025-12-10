package com.allo.backend.strategy;

import com.allo.backend.exception.ExternalApiException;
import com.allo.backend.model.HistoricalRates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoricalIDRUSDStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetchData() {
        log.info("Fetching historical IDR to USD rates from external API");

        try {
            HistoricalRates historicalRates = webClient.get()
                    .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                    .retrieve()
                    .bodyToMono(HistoricalRates.class)
                    .onErrorResume(e -> {
                        log.error("Error fetching historical rates: {}", e.getMessage());
                        return Mono.error(new ExternalApiException("Failed to fetch historical rates", e));
                    })
                    .block();

            if (historicalRates == null) {
                throw new ExternalApiException("Received null response from external API");
            }

            log.info("Successfully fetched historical IDR to USD rates");
            return historicalRates;

        } catch (Exception e) {
            log.error("Failed to fetch historical IDR to USD rates: {}", e.getMessage(), e);
            throw new ExternalApiException("Failed to fetch historical IDR to USD rates", e);
        }
    }
}
