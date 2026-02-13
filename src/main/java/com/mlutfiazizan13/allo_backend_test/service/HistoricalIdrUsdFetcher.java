package com.mlutfiazizan13.allo_backend_test.service;

import com.mlutfiazizan13.allo_backend_test.dto.HistoricalRatesResponse;
import com.mlutfiazizan13.allo_backend_test.exception.ExternalApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;

    public HistoricalIdrUsdFetcher(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Object fetchData() {
        try {
            return restTemplate.getForObject(
                    "/2024-01-01..2024-01-05?from=IDR&to=USD",
                    HistoricalRatesResponse.class);
        } catch (RestClientException ex) {
            throw new ExternalApiException(
                    "Failed to fetch historical IDR/USD rates from Frankfurter API", ex);
        }
    }

    @Override
    public String getStrategyType() {
        return "historical_idr_usd";
    }
}
