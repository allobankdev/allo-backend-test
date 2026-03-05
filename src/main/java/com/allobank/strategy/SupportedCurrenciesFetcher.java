package com.allobank.strategy;

import com.allobank.config.FrankfurterClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("supported_currencies")
@Slf4j
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public SupportedCurrenciesFetcher(FrankfurterClientFactory clientFactory) {
        this.restTemplate = clientFactory.getObject();
        this.baseUrl = clientFactory.getBaseUrl();
    }

    @Override
    public Object fetchData() {
        log.info("Fetching supported currencies...");
        
        String url = baseUrl + "/currencies";
        Map<String, String> currencies = restTemplate.getForObject(url, Map.class);
        
        if (currencies == null) {
            throw new RuntimeException("Failed to fetch currencies");
        }
        
        List<Map<String, String>> result = new ArrayList<>();
        for (Map.Entry<String, String> entry : currencies.entrySet()) {
            Map<String, String> item = new java.util.HashMap<>();
            item.put("code", entry.getKey());
            item.put("name", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }
}