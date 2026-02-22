package com.allo.backendtest.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private static final Logger log =
            LoggerFactory.getLogger(SupportedCurrenciesFetcher.class);

    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public List<Object> fetchAndTransform() {

        log.info("Fetching supported currencies...");

        Map<String, String> response =
                webClient.get()
                        .uri("/currencies")
                        .retrieve()
                        .onStatus(status -> status.is4xxClientError(),
                                res -> res.bodyToMono(String.class)
                                        .map(body -> new RuntimeException("Client error: " + body)))
                        .onStatus(status -> status.is5xxServerError(),
                                res -> res.bodyToMono(String.class)
                                        .map(body -> new RuntimeException("Server error: " + body)))
                        .bodyToMono(Map.class)
                        .block(Duration.ofSeconds(5));

        if (response == null || response.isEmpty()) {
            throw new IllegalStateException("Failed to fetch supported currencies");
        }

        return response.entrySet()
                .stream()
                .map(entry -> Map.of(
                        "currency", entry.getKey(),
                        "description", entry.getValue()
                ))
                .collect(Collectors.toList());
    }
}