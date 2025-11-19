package com.allobank.exercise.api.integration.impl;

import com.allobank.exercise.api.integration.FrankfurterClient;
import com.allobank.exercise.api.integration.dto.ExchangeHistoryResponse;
import com.allobank.exercise.api.properties.FrankfurterApiProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class FrankfurterClientImpl implements FrankfurterClient {

    private final WebClient webClient;
    private final FrankfurterApiProperties frankfurterApiProperties;

    public FrankfurterClientImpl(WebClient webClient, FrankfurterApiProperties frankfurterApiProperties) {
        this.webClient = webClient;
        this.frankfurterApiProperties = frankfurterApiProperties;
    }

    @Override
    public Object getLatestRates() {
        return null;
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
                .block();
    }

    @Override
    public Map<String, String> getSupportedCurrencies() {
        return webClient.get()
            .uri(frankfurterApiProperties.getCurrencyPath())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<LinkedHashMap<String, String>>() {})
            .block();
    }
}
