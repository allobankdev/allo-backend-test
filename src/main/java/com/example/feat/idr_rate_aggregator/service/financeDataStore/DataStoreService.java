package com.example.feat.idr_rate_aggregator.service.financeDataStore;

import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataStoreService {

    private Map<String, Object> dataStore = new HashMap<>();

    private volatile Map<String, Object> immutableDataStore = Collections.emptyMap();

    public void storeData(String key, Object data) {
        if (!immutableDataStore.isEmpty()) {
            throw new IllegalStateException("Cannot modify store after startup initialization.");
        }
        dataStore.put(key, data);
    }

    public void finalizeStore() {
        this.immutableDataStore = Collections.unmodifiableMap(new HashMap<>(dataStore));
        this.dataStore = Collections.emptyMap();
    }

    public Object getData(String key) {
        if (immutableDataStore.isEmpty()) {
            throw new RuntimeException("Data store not initialized. Please check application logs.");
        }
        return immutableDataStore.get(key);
    }

}
