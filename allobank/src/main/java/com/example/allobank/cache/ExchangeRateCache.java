package com.example.allobank.cache;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ExchangeRateCache {

    private volatile Map<String, Object> data = Map.of();

    public synchronized void load(Map<String, Object> newData) {
        this.data = Map.copyOf(newData);
    }

    public Object get(String resourceType) {
        return data.get(resourceType);
    }
}