package com.allobank.finance.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class InMemoryFinanceStore {
    private volatile Map<String, Object> data = Map.of();

    public synchronized void init(Map<String, Object> newData) {
        this.data = Map.copyOf(newData);
    }

    public Object getData(String key) {
        return data.get(key);
    }
}
