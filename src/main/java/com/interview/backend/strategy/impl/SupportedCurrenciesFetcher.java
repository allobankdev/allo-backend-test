package com.interview.backend.strategy.impl;

import com.interview.backend.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;

    @Value("${frankfurter.api.base-url:https://api.frankfurter.app}")
    private String baseUrl;

    @Override
    public Map<String, Object> fetchData() {
        try {
            String url = baseUrl + "/currencies";

            @SuppressWarnings("unchecked")
            Map<String, String> currencies = restTemplate.getForObject(url, Map.class);

            if (currencies == null) {
                throw new RuntimeException("Failed to fetch currencies - null response");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("currencies", currencies);
            result.put("count", currencies.size());

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch supported currencies: " + e.getMessage(), e);
        }
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }
}
