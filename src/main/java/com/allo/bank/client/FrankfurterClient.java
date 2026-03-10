package com.allo.bank.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.allo.bank.client.dto.FrankfurterHistoricalResponse;
import com.allo.bank.client.dto.FrankfurterLatestResponse;
import com.allo.bank.config.FrankfurterProperties;
import com.allo.bank.exception.ExternalApiException;

@Component
public class FrankfurterClient {

    private final RestClient restClient;
    private final FrankfurterProperties properties;

    public FrankfurterClient(@Qualifier("frankfurterRestClient") RestClient restClient,
                             FrankfurterProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public FrankfurterLatestResponse fetchLatestIdrRates() {
        try {
            return restClient.get()
                .uri(properties.getLatestPath())
                .retrieve()
                .body(FrankfurterLatestResponse.class);
        } catch (RestClientException exception) {
            throw new ExternalApiException("Failed to fetch latest IDR rates", exception);
        }
    }

    public FrankfurterHistoricalResponse fetchHistoricalIdrUsd() {
        try {
            return restClient.get()
                .uri(properties.getHistoricalPath())
                .retrieve()
                .body(FrankfurterHistoricalResponse.class);
        } catch (RestClientException exception) {
            throw new ExternalApiException("Failed to fetch historical IDR/USD rates", exception);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> fetchSupportedCurrencies() {
        try {
            return restClient.get()
                .uri(properties.getCurrenciesPath())
                .retrieve()
                .body(Map.class);
        } catch (RestClientException exception) {
            throw new ExternalApiException("Failed to fetch supported currencies", exception);
        }
    }
}
