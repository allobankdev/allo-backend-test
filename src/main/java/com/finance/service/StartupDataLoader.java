package com.finance.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StartupDataLoader implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> fetcherMap;
    private final AggregatedDataStore store;

    public StartupDataLoader(Map<String, IDRDataFetcher> fetcherMap, AggregatedDataStore store) {
        this.fetcherMap = fetcherMap;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, List<Map<String,Object>>> temp = new HashMap<>();
        fetcherMap.forEach((k, fetcher) -> {
            try {
                temp.put(k, fetcher.fetch());
            } catch (Exception e) {
                throw new IllegalStateException("Failed to load resource: " + k, e);
            }
        });
        store.initialize(temp);
    }
}
