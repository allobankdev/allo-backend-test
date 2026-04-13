package com.allobank.test.store;

import com.allobank.test.exception.DataNotInitializedException;
import com.allobank.test.exception.ResourceTypeNotSupportedException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class FinanceDataStore {

    private final AtomicReference<Map<String, Object>> dataRef = new AtomicReference<>();

    public void initializeOnce(Map<String, Object> initialData) {
        Map<String, Object> immutableData = deepImmutableMap(initialData);
        dataRef.compareAndSet(null, immutableData);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getByResourceType(String resourceType) {
        Map<String, Object> currentData = dataRef.get();
        if (currentData == null) {
            throw new DataNotInitializedException("Finance data is not initialized yet.");
        }

        Object value = currentData.get(resourceType);
        if (value == null) {
            throw new ResourceTypeNotSupportedException("Unsupported resourceType: " + resourceType);
        }
        return (List<Map<String, Object>>) value;
    }

    public List<String> supportedResourceTypes() {
        Map<String, Object> currentData = dataRef.get();
        if (currentData == null) {
            throw new DataNotInitializedException("Finance data is not initialized yet.");
        }
        return List.copyOf(currentData.keySet());
    }

    private static Map<String, Object> deepImmutableMap(Map<String, Object> source) {
        Map<String, Object> copied = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            copied.put(entry.getKey(), deepImmutableValue(entry.getValue()));
        }
        return Collections.unmodifiableMap(copied);
    }

    @SuppressWarnings("unchecked")
    private static Object deepImmutableValue(Object value) {
        if (value instanceof Map<?, ?> mapValue) {
            Map<String, Object> nested = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : mapValue.entrySet()) {
                nested.put(String.valueOf(entry.getKey()), deepImmutableValue(entry.getValue()));
            }
            return Collections.unmodifiableMap(nested);
        }

        if (value instanceof List<?> listValue) {
            List<Object> copied = listValue.stream()
                    .map(FinanceDataStore::deepImmutableValue)
                    .toList();
            return Collections.unmodifiableList(copied);
        }

        return value;
    }
}
