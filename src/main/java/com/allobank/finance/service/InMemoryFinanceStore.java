package com.allobank.finance.service;

import com.allobank.finance.exception.InvalidResourceException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InMemoryFinanceStore {
    private volatile Map<String, Object> data = Map.of();

    public synchronized void init(Map<String, Object> newData) {
        this.data = Map.copyOf(newData);
    }

    public Object getData(String key) {

        if(!data.containsKey(key)){
            throw new InvalidResourceException(key);
        }

        return data.get(key);
    }
}
