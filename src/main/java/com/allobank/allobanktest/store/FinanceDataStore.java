package com.allobank.allobanktest.store;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class FinanceDataStore {

    /**
     * Temporary mutable store during startup phase.
     */
    private final Map<String, Object> mutableStore = new HashMap<>();

    /**
     * Immutable store used at runtime.
     */
    private Map<String, Object> immutableStore;

    /**
     * Store data during application startup.
     */
    public synchronized void put(String resourceType, Object data) {
        if (immutableStore != null) {
            throw new IllegalStateException(
                    "Data store is already initialized and immutable"
            );
        }
        mutableStore.put(resourceType, data);
        log.debug("Stored data for resource: {}", resourceType);
    }

    /**
     * Lock the store after startup is completed.
     */
    public synchronized void lock() {
        this.immutableStore = Collections.unmodifiableMap(
                new HashMap<>(mutableStore)
        );
        mutableStore.clear();
        log.info("Finance data store locked and immutable");
    }

    /**
     * Read-only access for controllers.
     */
    public Object get(String resourceType) {
        if (immutableStore == null) {
            throw new IllegalStateException(
                    "Data store not initialized yet"
            );
        }
        return immutableStore.get(resourceType);
    }

}
