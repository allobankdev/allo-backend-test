package com.allo.backendtest.strategy;

import com.allo.backendtest.dto.HistoricalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private static final Logger log =
            LoggerFactory.getLogger(HistoricalIdrUsdFetcher.class);

    private final WebClient webClient;

    public HistoricalIdrUsdFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public List<Object> fetchAndTransform() {

        log.info("Fetching historical IDR-USD data...");

        HistoricalResponse response =
                webClient.get()
                        .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                        .retrieve()
                        .onStatus(status -> status.is4xxClientError(),
                                res -> res.bodyToMono(String.class)
                                        .map(body -> new RuntimeException("Client error: " + body)))
                        .onStatus(status -> status.is5xxServerError(),
                                res -> res.bodyToMono(String.class)
                                        .map(body -> new RuntimeException("Server error: " + body)))
                        .bodyToMono(HistoricalResponse.class)
                        .block(Duration.ofSeconds(5));

        if (response == null || response.rates() == null) {
            throw new IllegalStateException("Failed to fetch historical data");
        }

        return response.rates()
                .entrySet()
                .stream()
                .map(entry -> Map.of(
                        "date", entry.getKey(),
                        "USD", entry.getValue().get("USD")
                ))
                .collect(Collectors.toList());
    }
}