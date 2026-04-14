package com.allobank.finance.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.allobank.finance.config.FrankfurterApiProperties;

import java.util.Map;

@Component
public class FrankfurterClient {

    private final RestTemplate restTemplate;
    private final FrankfurterApiProperties properties;

    public FrankfurterClient(RestTemplate restTemplate, FrankfurterApiProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public Map<String, Object> fetchLatestIdrRatesRaw() {
        String url = "/latest?base=IDR";
        return getMap(url);
    }

    public Map<String, Object> fetchHistoricalIdrUsdRaw() {
        String url = "/" + properties.getHistoricalRange() + "?from=IDR&to=USD";
        return getMap(url);
    }

    public Map<String, String> fetchSupportedCurrenciesRaw() {
        String url = "/currencies";
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody() == null ? Map.of() : response.getBody();
    }

    private Map<String, Object> getMap(String url) {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody() == null ? Map.of() : response.getBody();
    }
}
