package com.thasya.frankfurter.store;

import com.thasya.frankfurter.exception.DataNotLoadedException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class FinanceDataStore {

    private final Map<String, List<?>> data = new ConcurrentHashMap<>();
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public void initialize(Map<String, List<?>> initialData) {
        if (!initialized.compareAndSet(false, true)) {
            throw new IllegalStateException("FinanceDataStore already initialized");
        }

        data.clear();
        initialData.forEach((key, value) ->
                data.put(key, Collections.unmodifiableList(value))
        );
    }

    public List<?> getData(String resourceType) {
        if (!initialized.get()) {
            throw new DataNotLoadedException("Data store not initialized yet");
        }
        List<?> result = data.get(resourceType);
        if (result == null) {
            throw new DataNotLoadedException("No data for resource type: " + resourceType);
        }
        return result;
    }
}
