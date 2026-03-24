package com.allobank.test.store;

import com.allobank.test.exception.DataNotInitializedException;
import com.allobank.test.exception.ResourceTypeNotSupportedException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class FinanceDataStore {

    private final AtomicReference<Map<String, Object>> dataRef = new AtomicReference<>();

    public void initializeOnce(Map<String, Object> initialData) {
        Map<String, Object> immutableData = Map.copyOf(initialData);
        dataRef.compareAndSet(null, immutableData);
    }

    public Object getByResourceType(String resourceType) {
        Map<String, Object> currentData = dataRef.get();
        if (currentData == null) {
            throw new DataNotInitializedException("Finance data is not initialized yet.");
        }

        Object value = currentData.get(resourceType);
        if (value == null) {
            throw new ResourceTypeNotSupportedException("Unsupported resourceType: " + resourceType);
        }
        return value;
    }

    public List<String> supportedResourceTypes() {
        Map<String, Object> currentData = dataRef.get();
        if (currentData == null) {
            throw new DataNotInitializedException("Finance data is not initialized yet.");
        }
        return List.copyOf(currentData.keySet());
    }
}
