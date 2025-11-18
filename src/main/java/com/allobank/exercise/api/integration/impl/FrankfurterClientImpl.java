package com.allobank.exercise.api.integration.impl;

import com.allobank.exercise.api.integration.FrankfurterClient;
import com.allobank.exercise.api.properties.FrankfurterApiProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;

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
    public Object getHistoricalRates()
    {

        return null;
    }

    @Override
    public LinkedHashMap<String, String> getSupportedCurrencies() {
        return webClient.get()
            .uri(frankfurterApiProperties.getCurrencyPath())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<LinkedHashMap<String, String>>() {})
            .block();
    }
}
