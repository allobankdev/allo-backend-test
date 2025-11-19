package com.finance.service;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AggregatedDataStore {
    private volatile Map<String, List<Map<String,Object>>> store = Map.of();

    public boolean contains(String key) { return store.containsKey(key); }
    public List<Map<String,Object>> get(String key) { return store.get(key); }

    public synchronized void initialize(Map<String, List<Map<String,Object>>> initial) {
        if (!store.isEmpty()) throw new IllegalStateException("already initialized");
        this.store = Map.copyOf(initial);
    }
}
