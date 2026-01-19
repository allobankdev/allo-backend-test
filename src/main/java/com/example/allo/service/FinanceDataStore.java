package com.example.allo.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FinanceDataStore {

    private Map<String, Object> data = Map.of();

    public synchronized void init(Map<String, Object> loaded) {
        this.data = Map.copyOf(loaded);
    }

    public Object get(String key) {
        return data.get(key);
    }
}

