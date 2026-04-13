package com.allobank.test.strategy;

import com.allobank.test.client.FrankfurterClient;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final FrankfurterClient frankfurterClient;

    public SupportedCurrenciesFetcher(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public String resourceType() {
        return "supported_currencies";
    }

    @Override
    public List<Map<String, Object>> fetch() {
        Map<String, String> rawResponse = frankfurterClient.fetchSupportedCurrenciesRaw();
        return rawResponse.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("resourceType", resourceType());
                    result.put("code", entry.getKey());
                    result.put("name", entry.getValue());
                    return result;
                })
                .toList();
    }
}
