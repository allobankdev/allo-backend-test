package com.allobank.finance.client;

import com.allobank.finance.config.FrankfurterProperties;
import com.allobank.finance.exception.ResourceTypeNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FrankfurterClient {

    private final RestTemplate restTemplate;
    private final FrankfurterProperties properties;

    public Map<String, Object> getLatestIdrRates() {
        return get(properties.getLatestIdr());
    }

    public Map<String, Object> getHistoricalIdrUsd() {
        return get(properties.getHistoricalIdrUsd());
    }

    public Map<String, Object> getSupportedCurrencies() {
        return get(properties.getCurrencies());
    }

    private Map<String, Object> get(String path) {
        try {
            String url = properties.getBaseUrl() + path;
            log.info("Calling Frankfurter API: {}", url);
            return restTemplate.getForObject(url, Map.class);
        } catch (RestClientException e) {
            log.error("Frankfurter API error", e);
            throw new ResourceTypeNotFoundException("Frankfurter API unavailable", e);
        }
    }
}
