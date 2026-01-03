package com.allobank.allobackendtest.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class InMemoryDataStore {

    private final Map<String, Object> dataStore = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        dataStore.put(key, value);
    }

    public Object get(String key) {
        return dataStore.get(key);
    }

    public Map<String, Object> getAll() {
        return dataStore;
    }

    public boolean containsKey(String key) {
        return dataStore.containsKey(key);
    }

}
