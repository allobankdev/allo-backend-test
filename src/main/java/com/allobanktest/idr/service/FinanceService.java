package com.allobanktest.idr.service;

import com.allobanktest.idr.store.DataStore;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FinanceService {

    private final DataStore dataStore;

    public FinanceService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Map<String, Object> getData(String resourceType) {
        Map<String, Object> payload = dataStore.get(resourceType);
        if (payload == null) return null;
        return payload;
    }
}

