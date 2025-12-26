package com.allobank.finance.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinanceDataStore {

    private final Map<String, Object> data = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    public void put(String key, Object value) {
        if (initialized) {
            throw new IllegalStateException("Data already initialized");
        }
        data.put(key, value);
    }

    public void freeze() {
        initialized = true;
    }

    public Object get(String key) {
        return data.get(key);
    }
}

