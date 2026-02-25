package com.allobank.finance.cache;

import com.allobank.finance.exception.ErrorCode;
import com.allobank.finance.model.FinanceData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class IDRDataFetcherCache {

    private final Map<String, FinanceData> cache = new ConcurrentHashMap<>();
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public void put(String key, FinanceData data) {
        if (initialized.get()) {
            throw ErrorCode.CACHE_IMMUTABLE.toException();
        }

        cache.put(key, data);
        log.debug("Cached data for resource type: {}", key);
    }

    public void markInitialized() {
        if (!initialized.compareAndSet(false, true)) {
            throw ErrorCode.CACHE_ALREADY_INITIALIZED.toException();
        }
        log.info("Cache marked as initialized and immutable with {} entries", cache.size());
    }

    public FinanceData get(String key) {
        if (!initialized.get()) {
            throw ErrorCode.CACHE_NOT_INITIALIZED.toException();
        }

        FinanceData data = cache.get(key);
        if (data == null) {
            throw ErrorCode.RESOURCE_NOT_FOUND.toException(
                "Data for resource type '" + key + "' not found"
            );
        }
        return data;
    }
}
