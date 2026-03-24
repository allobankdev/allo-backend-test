package com.allobank.test.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceDataService {

    private final Map<String, Object> data;

    public FinanceDataService() {
        Map<String, Object> seed = new LinkedHashMap<>();

        seed.put("latest_idr_rates", Map.of(
                "base", "EUR",
                "date", "2026-03-24",
                "rates", Map.of(
                        "IDR", 17650.12,
                        "USD", 1.0842)));

        seed.put("historical_idr_usd", List.of(
                Map.of("date", "2026-03-20", "idr_per_usd", 16310.51),
                Map.of("date", "2026-03-21", "idr_per_usd", 16302.14),
                Map.of("date", "2026-03-22", "idr_per_usd", 16324.63)));

        seed.put("supported_currencies", List.of(
                "USD", "EUR", "IDR", "JPY", "SGD"));

        this.data = Map.copyOf(seed);
    }

    public Object findByResourceType(String resourceType) {
        Object value = data.get(resourceType);
        if (value == null) {
            throw new IllegalArgumentException("Unsupported resourceType: " + resourceType);
        }
        return value;
    }

    public List<String> supportedResourceTypes() {
        return List.copyOf(data.keySet());
    }
}
