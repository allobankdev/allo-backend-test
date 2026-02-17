package com.exchange.frankfurter.store;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class FinanceDataStore {

    private Map<String, Object> dataStore;

    public void initialize(Map<String, Object> data) {
        this.dataStore = Collections.unmodifiableMap(data);
    }

    public Object get(String key) {
        return dataStore.get(key);
    }

    public Map<String, Object> getAll() {
        return dataStore;
    }
}
