package com.example.finance.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;

    @Autowired
    public SupportedCurrenciesFetcher(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public List<Map<String, Object>> fetchData() {
        String url = "https://api.frankfurter.app/currencies";
        Map<String, String> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> result = new ArrayList<>();
        if (response != null) {
            for (Map.Entry<String, String> entry : response.entrySet()) {
                result.add(Map.of("currency", entry.getKey(), "name", entry.getValue()));
            }
        }
        return result;
    }
}