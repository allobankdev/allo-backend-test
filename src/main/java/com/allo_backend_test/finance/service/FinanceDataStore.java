
package com.allo_backend_test.finance.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinanceDataStore {

    private final Map<String, Object> store = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    public synchronized void loadData(Map<String, Object> data) {
        if (!initialized) {
            store.putAll(data);
            initialized = true;
        }
    }

    public Object getData(String resourceType) {
        return store.get(resourceType);
    }

    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(store);
    }
}
