package com.example.allo_bank.service;

import com.example.allo_bank.strategy.IDRDataFetcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StrategyRegistry {

    private final Map<String, IDRDataFetcher> fetcherMap = new HashMap<>();

    public StrategyRegistry(List<IDRDataFetcher> fetchers) {
        for (IDRDataFetcher f :fetchers) {
            fetcherMap.put(f.getResourceName(), f);
        }
    }

    public IDRDataFetcher get(String resourceType) {
        return fetcherMap.get(resourceType);
    }
}
