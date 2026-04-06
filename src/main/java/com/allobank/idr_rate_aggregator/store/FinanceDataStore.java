package com.allobank.idr_rate_aggregator.store;

import com.allobank.idr_rate_aggregator.model.FinanceData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class FinanceDataStore {

    // Volatile memastikan visibility across threads
    private volatile Map<String, FinanceData> store = Collections.emptyMap();

    // Flag untuk memastikan data hanya bisa di-load sekali
    private final AtomicBoolean loaded = new AtomicBoolean(false);

    public void loadData(Map<String, FinanceData> data) {
        if (!loaded.compareAndSet(false, true)) {
            log.warn("Data store already loaded. Ignoring reload attempt.");
            return;
        }

        // Collections.unmodifiableMap memastikan immutability setelah load
        this.store = Collections.unmodifiableMap(data);
        log.info("Finance data store loaded successfully with {} resources.", data.size());
    }

    public FinanceData get(String resourceType) {
        return store.get(resourceType);
    }

    public boolean isLoaded() {
        return loaded.get();
    }

    public Map<String, FinanceData> getAll() {
        return store;
    }
}