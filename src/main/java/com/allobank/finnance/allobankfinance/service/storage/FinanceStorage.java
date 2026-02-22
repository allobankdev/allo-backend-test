package com.allobank.finnance.allobankfinance.service.storage;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FinanceStorage {

    private Map<String, Object> data;

    public synchronized void setData(Map<String, Object> loadedData) {
        if (this.data != null) {
            throw new IllegalStateException("Data already initialized");
        }
        this.data = Map.copyOf(loadedData);
    }

    public Map<String, Object> getData() {
        if (data == null) {
            throw new IllegalStateException("Data not initialized yet");
        }
        return data;
    }
}
