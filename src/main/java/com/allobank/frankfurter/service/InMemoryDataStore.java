package com.allobank.frankfurter.service;

import com.allobank.frankfurter.model.DataResult;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryDataStore {

    private final Map<String, DataResult> store = new ConcurrentHashMap<>();

    public void put(DataResult result) {
        store.put(result.getResourceType(), result);
    }

    public DataResult get(String resourceType) {
        return store.get(resourceType);
    }

    public Map<String, DataResult> getAll() {
        return Map.copyOf(store);
    }
}