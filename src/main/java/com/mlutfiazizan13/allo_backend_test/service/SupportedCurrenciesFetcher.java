package com.mlutfiazizan13.allo_backend_test.service;

import com.mlutfiazizan13.allo_backend_test.dto.CurrencyMapResponse;
import com.mlutfiazizan13.allo_backend_test.exception.ExternalApiException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;

    public SupportedCurrenciesFetcher(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Object fetchData() {
        try {
            Map<String, String> currencies = restTemplate.exchange(
                    "/currencies",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, String>>() {}
            ).getBody();
            return new CurrencyMapResponse(currencies);
        } catch (RestClientException ex) {
            throw new ExternalApiException(
                    "Failed to fetch supported currencies from Frankfurter API", ex);
        }
    }

    @Override
    public String getStrategyType() {
        return "supported_currencies";
    }
}
