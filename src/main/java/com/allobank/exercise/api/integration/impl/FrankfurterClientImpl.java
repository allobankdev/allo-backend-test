package com.allobank.exercise.api.integration.impl;

import com.allobank.exercise.api.integration.FrankfurterClient;
import com.allobank.exercise.api.integration.dto.ExchangeHistoryResponse;
import com.allobank.exercise.api.integration.dto.ExchangeRateResponse;
import com.allobank.exercise.api.properties.FrankfurterApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;

@Service
public class FrankfurterClientImpl implements FrankfurterClient {

    private final WebClient webClient;
    private final FrankfurterApiProperties frankfurterApiProperties;
    private static final Logger log = LoggerFactory.getLogger(FrankfurterClientImpl.class);

    public FrankfurterClientImpl(WebClient webClient, FrankfurterApiProperties frankfurterApiProperties) {
        this.webClient = webClient;
        this.frankfurterApiProperties = frankfurterApiProperties;
    }

    @Override
    public ExchangeRateResponse getLatestRates() {
        return webClient.get()
            .uri(frankfurterApiProperties.getLatestIdrPath())
            .retrieve()
            .bodyToMono(ExchangeRateResponse.class)
            .onErrorResume(ex -> {
                log.error("Failed to getLatestRates: {}", ex.getMessage());
                return Mono.just(new ExchangeRateResponse());
            })
            .block();
    }

    @Override
    public ExchangeHistoryResponse getExchangeHistory
    (
        String queryTime,
        String fromCurrency,
        String toCurrency
    )
    {
        String urlBuilder = frankfurterApiProperties.getBaseUrl() +
                String.format("/%s?from=%s&to=%s", queryTime, fromCurrency, toCurrency);

        return webClient.get()
            .uri(urlBuilder)
            .retrieve()
            .bodyToMono(ExchangeHistoryResponse.class)
            .onErrorResume(ex -> {
                log.error("Failed to getExchangeHistory: {}", ex.getMessage());
                return Mono.just(new ExchangeHistoryResponse());
            })
            .block();
    }

    @Override
    public Map<String, String> getSupportedCurrencies() {
        return webClient.get()
            .uri(frankfurterApiProperties.getCurrencyPath())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
            .onErrorResume(ex -> {
                log.error("Failed to getSupportedCurrencies: {}", ex.getMessage());
                return Mono.just(new HashMap<>());
            })
            .block();
    }
}
