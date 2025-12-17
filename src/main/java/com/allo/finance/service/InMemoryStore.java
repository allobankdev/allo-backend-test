package com.allo.finance.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStore {

    private final Map<String, Object> store = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        store.put(key, value);
    }

    public Object get(String key) {
        return store.get(key);
    }
}
