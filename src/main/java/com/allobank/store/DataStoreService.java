package com.allobank.store;

import com.allobank.enums.ResourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataStoreService {


    private final ConcurrentHashMap<String, Object> mutableStore = new ConcurrentHashMap<>();


    private final AtomicReference<Map<String, Object>> frozenStore = new AtomicReference<>(null);

    public boolean isInitialized() {
        return frozenStore.get() != null;
    }

    /**
     * Store data before initialization (bootstrap phase)
     * Non-blocking and thread-safe.
     */
    public void storeData(ResourceType resourceType, Object data) {
        if (isInitialized()) {
            throw new IllegalStateException("DataStore is already initialized and immutable");
        }

        String key = resourceType.getValue();
        mutableStore.put(key, data);
        log.info("Stored data for resource type: {}", key);
    }

    /**
     * Freeze the store to make it immutable.
     * Only first caller succeeds; others do nothing.
     */
    public void markInitialized() {
        if (isInitialized()) return;

        Map<String, Object> immutableCopy = Map.copyOf(mutableStore);

        if (frozenStore.compareAndSet(null, immutableCopy)) {
            log.info("Data store frozen with {} resources", immutableCopy.size());
            mutableStore.clear();
        }
    }

    /**
     * Retrieve value for a resource after initialization.
     */
    public Object getData(String resourceType) {
        Map<String, Object> store = Optional.ofNullable(frozenStore.get())
                .orElseThrow(() -> new IllegalStateException("Data store not yet initialized"));
        return Optional.ofNullable(store.get(resourceType))
                .orElseThrow(() -> new IllegalArgumentException("Unknown resource type: " + resourceType));
    }

    /**
     * Get entire frozen data map
     */
    public Map<String, Object> getAllData() {
        return Optional.ofNullable(frozenStore.get())
                .orElseThrow(() -> new IllegalStateException("Data store not yet initialized"));
    }

}
