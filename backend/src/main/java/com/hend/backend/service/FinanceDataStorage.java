package com.hend.backend.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : hend wunga
 */

@Service
public class FinanceDataStorage {

    // Menggunakan ConcurrentHashMap untuk keamanan thread saat pengisian awal
    private Map<String, Object> storage = new ConcurrentHashMap<>();
    private final AtomicBoolean isLocked = new AtomicBoolean(false);

    public void saveData(String resourceType, Object data) {
        if (!isLocked.get()) {
            storage.put(resourceType, data);
        } else {
            throw new IllegalStateException("Storage already locked");
        }
    }

    public Object getData(String resourceType) {
        return storage.get(resourceType);
    }

    public void lockStorage() {
        if (isLocked.compareAndSet(false, true)) {
            this.storage = Collections.unmodifiableMap(this.storage);
        }
    }
}
