package com.allobank.idrrates.service;

import com.allobank.idrrates.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class DataStoreService {

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private volatile Map<String, List<?>> store = Collections.emptyMap();

    public void initialize(Map<String, List<?>> data) {
        if (!initialized.compareAndSet(false, true)) {
            throw new IllegalStateException("DataStoreService has already been initialized");
        }
        this.store = Collections.unmodifiableMap(data);
    }

    public List<?> getData(String resourceType) {
        if (!initialized.get()) {
            throw new IllegalStateException("DataStoreService has not been initialized yet");
        }
        List<?> data = store.get(resourceType);
        if (data == null) {
            throw new ResourceNotFoundException("Unknown resource type: " + resourceType);
        }
        return data;
    }

    public boolean isInitialized() {
        return initialized.get();
    }
}
