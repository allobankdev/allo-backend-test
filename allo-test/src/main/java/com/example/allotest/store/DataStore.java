package com.example.allotest.store;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataStore {
    private final Map<String, Object[]> store = new ConcurrentHashMap<>();
    private boolean loaded = false;

    public void putIntoStore(String key, Object[] value) {
        if (!loaded) {
            store.put(key, value);
        }
    }

    public Object[] getFromStore(String key) {
        return store.get(key);
    }

    public void makeLoaded() {
        loaded = true;
    }
}
