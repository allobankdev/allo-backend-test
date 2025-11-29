package com.allobank.backendtest.service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Thread-safe immutable store. initialize(...) must be called exactly once.
 */
public class ImmutableFinanceStore {

    // IMPORTANT:
    // DO NOT USE Collections.emptyMap() → returns internal JDK map
    // that causes Jackson "No acceptable representation" errors.
    private static final Map<String, List<?>> EMPTY = new HashMap<>();

    private final AtomicReference<Map<String, List<?>>> storeRef =
            new AtomicReference<>(EMPTY);

    /**
     * Initialize the store exactly once. After initialization, data is immutable and reads are lock-free.
     */
    public void initialize(Map<String, List<?>> initial) {

        Map<String, List<?>> prepared =
                initial.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> Collections.unmodifiableList(
                                        new ArrayList<>(e.getValue() == null
                                                ? Collections.emptyList()
                                                : e.getValue())
                                )
                        ));

        // Make the map immutable too
        Map<String, List<?>> wrapped = Collections.unmodifiableMap(prepared);

        if (!storeRef.compareAndSet(EMPTY, wrapped)) {
            throw new IllegalStateException("Store already initialized");
        }
    }

    /**
     * Retrieve list by key.
     */
    public List<?> get(String key) {
        return storeRef.get().getOrDefault(key, Collections.emptyList());
    }

    /**
     * Returns true if store has been initialized.
     */
    public boolean isInitialized() {
        return storeRef.get() != EMPTY;
    }
}
