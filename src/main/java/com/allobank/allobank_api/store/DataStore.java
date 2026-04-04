package com.allobank.allobank_api.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class DataStore {
    private final Map<String, Object> store = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        store.put(key, value);
    }

    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(store.get(key));
    }
    
}
