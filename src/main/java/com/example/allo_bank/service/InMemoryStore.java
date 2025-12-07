package com.example.allo_bank.service;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Service
public class InMemoryStore {

    private Map<String, Object> data = Collections.emptyMap();

    public synchronized void setData (Map<String, Object> newData) {
        this.data = Collections.unmodifiableMap(newData);
    }

    public Object get (String key) {
        return data.get(key);
    }
}
