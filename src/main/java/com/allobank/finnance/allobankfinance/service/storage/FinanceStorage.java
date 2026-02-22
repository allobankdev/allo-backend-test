package com.allobank.finnance.allobankfinance.service.storage;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class FinanceStorage {

    private Map<String, Object> data;

    public synchronized void initialize(Map<String, Object> newData) {
        if (this.data != null) {
            throw new IllegalStateException("Storage already initialized");
        }
        this.data = Collections.unmodifiableMap(newData);
    }
    
}
