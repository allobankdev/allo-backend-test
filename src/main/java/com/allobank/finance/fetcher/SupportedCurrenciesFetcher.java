package com.allobank.finance.fetcher;

import com.allobank.finance.dto.FinanceDataResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "supported_currencies";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Override
    public FinanceDataResponse fetch() {
        log.info("Fetching supported currencies from Frankfurter API...");

        try {
            String rawJson = webClient.get()
                    .uri("/currencies")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(180))
                    .block();

            if (rawJson == null || rawJson.isBlank()) {
                throw new IllegalStateException("Empty response from Frankfurter API for supported currencies");
            }

            Map<String, String> currencies = objectMapper.readValue(
                    rawJson, new TypeReference<Map<String, String>>() {}
            );

            log.info("Supported currencies fetched successfully. Total currencies: {}", currencies.size());

            return FinanceDataResponse.builder()
                    .resourceType(RESOURCE_TYPE)
                    .fetchedAt(Instant.now().toString())
                    .data(currencies)
                    .build();

        } catch (WebClientResponseException ex) {
            log.error("HTTP error fetching supported currencies: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch supported currencies: HTTP " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            log.error("Unexpected error fetching supported currencies", ex);
            throw new RuntimeException("Failed to fetch supported currencies", ex);
        }
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }
}
