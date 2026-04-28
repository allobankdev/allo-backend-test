package com.finance.aggregator.service.impl;

import com.finance.aggregator.service.DataStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class DataStoreServiceImpl implements DataStoreService {

    private Map<String, Object> storage = new ConcurrentHashMap<>();
    private final AtomicBoolean loaded = new AtomicBoolean(false);
    private static final int EXPECTED_COUNT = 3;

    @Override
    public void simpanData(String resourceType, Object data) {
        if (loaded.get()) {
            throw new IllegalStateException("Cannot modify data store after loading is complete");
        }

        if (resourceType == null || resourceType.trim().isEmpty()) {
            throw new IllegalArgumentException("Resource type cannot be null or empty");
        }

        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        log.info("Storing data for: {}", resourceType);
        storage.put(resourceType, data);

        if (storage.size() == EXPECTED_COUNT && !loaded.get()) {
            loaded.set(true);
            storage = Collections.unmodifiableMap(storage);
            log.info("All data loaded successfully! Storage is now immutable.");
            log.info("Stored resources: {}", storage.keySet());
        }
    }

    @Override
    public Object ambilData(String resourceType) {
        Object data = storage.get(resourceType);
        if (data == null) {
            log.warn("Data not found for: {}", resourceType);
        }
        return data;
    }

    @Override
    public boolean isDataLengkap() {
        return storage.size() == EXPECTED_COUNT;
    }

    @Override
    public Map<String, Object> getAllData() {
        return storage;
    }

    @Override
    public boolean isLoaded() {
        return loaded.get();
    }
}