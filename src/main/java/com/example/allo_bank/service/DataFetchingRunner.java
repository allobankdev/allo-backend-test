package com.example.allo_bank.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataFetchingRunner implements ApplicationRunner {

    private final StrategyRegistry registry;
    private final InMemoryStore store;

    public DataFetchingRunner(StrategyRegistry registry, InMemoryStore store) {
        this.registry = registry;
        this.store = store;
    }

    public void run(ApplicationArguments args) {
        Map<String, Object> result = new HashMap<>();

        registry.get("latest_idr_rates").safeFetch();
        registry.get("historical_idr_usd").safeFetch();
        registry.get("supported_currencies").safeFetch();

        result.put("latest_idr_rates", registry.get("latest_idr_rates").safeFetch());
        result.put("historical_idr_usd", registry.get("historical_idr_usd").safeFetch());
        result.put("supported_currencies", registry.get("supported_currencies").safeFetch());

        store.setData(result);

    }
}
