package com.example.AlloBank.client;

import com.example.AlloBank.exception.ExternalServiceException;
import com.example.AlloBank.response.CurrenciesResponse;
import com.example.AlloBank.response.HistoricalRatesResponse;
import com.example.AlloBank.response.LatestRatesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class FrankfurterClient {

    private final RestTemplate restTemplate;

    public FrankfurterClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LatestRatesResponse getLatestRates() {
        try {
            return restTemplate.getForObject(
                    "/latest?base=IDR",
                    LatestRatesResponse.class
            );
        } catch (RestClientException e) {
            throw new ExternalServiceException("Failed to fetch latest IDR rates", e);
        }
    }

    public HistoricalRatesResponse getHistoricalUsd() {
        try {
            return restTemplate.getForObject(
                    "/2024-01-01..2024-01-05?from=IDR&to=USD",
                    HistoricalRatesResponse.class
            );
        } catch (RestClientException e) {
            throw new ExternalServiceException("Failed to fetch historical USD", e);
        }
    }

    public CurrenciesResponse getCurrencies() {
        try {
            return restTemplate.getForObject(
                    "/currencies",
                    CurrenciesResponse.class
            );
        } catch (RestClientException e) {
            throw new ExternalServiceException("Failed to fetch currencies", e);
        }
    }

}
