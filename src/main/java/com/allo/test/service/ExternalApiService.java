package com.allo.test.service;

import com.allo.test.dto.LatestRatesResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
public class ExternalApiService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ExternalApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getCurrencies() {
        try {
            return webClient.get()
                    .uri("/currencies")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("External API error: " + e.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException("Failed to call external API");
        }
    }
    public Map<String, String> getCurrenciesParsed() {
        try {
            String response = getCurrencies();
            return objectMapper.readValue(response, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse currencies");
        }
    }
    public Map<String, Object> getHistoricalRatesParsed() {
        try {
            String response = getHistoricalRates();
            return objectMapper.readValue(
                    response,
                    new TypeReference<Map<String, Object>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse historical data");
        }
    }
    public String getHistoricalRates() {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/2024-01-01..2024-01-05")
                            .queryParam("from", "IDR")
                            .queryParam("to", "USD")
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            return "{\"error\": \"failed\"}";
        }
    }

    public LatestRatesResponse getLatestRatesParsed() {
        try {
            String response = getLatestRates();

            return objectMapper.readValue(
                    response,
                    LatestRatesResponse.class
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse latest rates");
        }
    }

    public String getLatestRates(){
        try {
            return webClient.get()
                    .uri("/latest?base=IDR")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            return "{\"error\": \"failed\"}";
        }
    }

}
