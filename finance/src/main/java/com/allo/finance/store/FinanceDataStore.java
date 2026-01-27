package com.allo.finance.store;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class FinanceDataStore {

    private Map<String, Object> data = new HashMap<>();

    /**
     * Dipanggil SEKALI saat startup
     */
    public void initialize(Map<String, Object> loadedData) {
        this.data = Collections.unmodifiableMap(new HashMap<>(loadedData));
    }

    /**
     * Dipanggil oleh Controller (read-only)
     */
    public Object getData(String resourceType) {
        return data.get(resourceType);
    }
}
