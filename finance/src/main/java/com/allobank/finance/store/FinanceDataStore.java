package com.allobank.finance.store;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

@Component
public class FinanceDataStore {

    private Map<String, Object> data;

    private final AtomicBoolean init = new AtomicBoolean(false);

    public void initialize(Map<String, Object> loadedData) {
        if (init.compareAndSet(false, true)) {
            this.data = Map.copyOf(loadedData);
        } else {
            throw new IllegalStateException("FinanceDataStore already initialized");
        }
    }

    public Object get(String resourceType) {
        if (!init.get()){
            throw new IllegalStateException("Finance data not initialized yet!");
        }
        return data.get(resourceType);
    }

    public boolean isInitialized(){
        return init.get();
    }
}
