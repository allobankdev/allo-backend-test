package com.allo.backend.store;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class DataStore {
    private Map<String, Object> data;

    public synchronized void setData(Map<String, Object> data) {
        this.data = Map.copyOf(data);
    }

    public Object get(String type) {
        return data.get(type);
    }

}