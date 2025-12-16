package com.allo.backend.service;

import com.allo.backend.strategy.IDRDataFetcher;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceDataStoreService {
    private final Map<String, Object> dataStore = new HashMap<>();
    private boolean initialized = false;

    public synchronized void loadData(List<IDRDataFetcher> fetchers) {
        if (initialized) return;
        for (IDRDataFetcher fetcher : fetchers) {
            dataStore.put(fetcher.getResourceType(), fetcher.fetchData());
        }
        initialized = true;
    }

    public Object getData(String resourceType) {
        return dataStore.get(resourceType);
    }

    public Map<String, Object> getAllData() {
        return Collections.unmodifiableMap(dataStore);
    }
}
