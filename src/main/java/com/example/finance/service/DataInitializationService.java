package com.example.finance.service;

import org.springframework.stereotype.Service;

import com.example.finance.strategy.IDRDataFetcher;

import java.util.Map;

@Service
public class DataInitializationService {

    private final Map<String, IDRDataFetcher> fetchers;
    private final InMemoryDataStore dataStore;

    public DataInitializationService(Map<String, IDRDataFetcher> fetchers,
                                     InMemoryDataStore dataStore) {
        this.fetchers = fetchers;
        this.dataStore = dataStore;
    }

    public void loadAllData() {

        for (Map.Entry<String, IDRDataFetcher> entry : fetchers.entrySet()) {

            String key = entry.getKey();
            IDRDataFetcher fetcher = entry.getValue();

            Object data = fetcher.fetchData();

            dataStore.put(key, data);
        }
    }
}