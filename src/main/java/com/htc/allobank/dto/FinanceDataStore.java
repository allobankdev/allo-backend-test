package com.htc.allobank.dto;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FinanceDataStore {
    private final Map<String,Object> data;

    public FinanceDataStore(Map<String,Object> source) {
        this.data = Collections.unmodifiableMap(new ConcurrentHashMap<>(source));
    }

    public Object get(String resourceType) {
        return data.get(resourceType);
    }

    public Map<String,Object> getAll() {
        return data;
    }
}
