package com.allobank.finance.store;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

/**
 * Thread-safe, immutable data store for IDR finance data.
 * Data is loaded once on startup and served from cache thereafter.
 */
@Component
public class IDRDataStore {

    private final AtomicReference<Map<String, Object>> cache = new AtomicReference<>(Collections.unmodifiableMap(new HashMap<>()));

    /**
     * Stores data fetched from external APIs
     * @param resourceType the type of resource (latest_idr_rates, historical_idr_usd, supported_currencies)
     * @param data the fetched data
     */
    public void store(String resourceType, Object data) {
        Map<String, Object> currentCache = new HashMap<>(cache.get());
        currentCache.put(resourceType, data);
        cache.set(Collections.unmodifiableMap(currentCache));
    }

    /**
     * Retrieves data for a specific resource type
     * @param resourceType the type of resource
     * @return the cached data, or null if not found
     */
    public Object get(String resourceType) {
        return cache.get().get(resourceType);
    }

    /**
     * Retrieves all cached data
     * @return an immutable view of the cache
     */
    public Map<String, Object> getAll() {
        return cache.get();
    }

    /**
     * Checks if data exists for a resource type
     * @param resourceType the type of resource
     * @return true if data exists, false otherwise
     */
    public boolean contains(String resourceType) {
        return cache.get().containsKey(resourceType);
    }
}
