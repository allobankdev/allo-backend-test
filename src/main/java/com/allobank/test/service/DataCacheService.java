package com.allobank.test.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dibuat oleh: Andre Rizaldi Brillianto
 * Email: andrerizaldib@gmail.com
 * Date: Wednesday, 7-January-2026
 * description: allo-bank-test
 */
@Service
public class DataCacheService {

    private Map<String, Object> storage = new ConcurrentHashMap<>();
    private boolean initialized = false;

    // cek apakah sudah di initialized
    // menjadikan penyimpanan unmodifiable, tidak bisa diubah
    // ketika sudah di initialized = true
    public synchronized void initializeData(Map<String, Object> data) {
        if (initialized) {
            throw new IllegalStateException("Data cache already initialized");
        }
        // Make the outer map unmodifiable.
        this.storage = Collections.unmodifiableMap(new ConcurrentHashMap<>(data));
        this.initialized = true;
    }

    public Object getData(String resourceType) {
        if (!initialized) {
            throw new IllegalStateException("Data cache not yet initialized");
        }
        return storage.get(resourceType);
    }
}
