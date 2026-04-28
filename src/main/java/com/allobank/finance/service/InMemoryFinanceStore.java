package com.allobank.finance.service;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@Component
public class InMemoryFinanceStore {

    private static final Logger log = Logger.getLogger(InMemoryFinanceStore.class.getName());

    private final ConcurrentHashMap<String, List<Map<String, Object>>> store = new ConcurrentHashMap<>();
    private final AtomicBoolean sealed = new AtomicBoolean(false);

    public void put(String resourceType, List<Map<String, Object>> data) {
        if (sealed.get()) {
            throw new IllegalStateException(
                    "Store sudah sealed. Data tidak bisa diubah setelah startup.");
        }
        store.put(resourceType, Collections.unmodifiableList(data));
        log.info(String.format("[InMemoryFinanceStore] Tersimpan %d record untuk: '%s'",
                data.size(), resourceType));
    }

    public void seal() {
        sealed.set(true);
        log.info("[InMemoryFinanceStore] Store sudah sealed. Semua data immutable.");
    }

    public Optional<List<Map<String, Object>>> get(String resourceType) {
        return Optional.ofNullable(store.get(resourceType));
    }

    public boolean isSealed() {
        return sealed.get();
    }

    public Set<String> getResourceTypes() {
        return Collections.unmodifiableSet(store.keySet());
    }
}