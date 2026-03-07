package com.allo.store;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.allo.dto.FinanceResourceResponse;
import com.allo.exception.DataNotLoadedException;
import com.allo.exception.ResourceNotFoundException;

@Component
public class FinanceDataStore {

    private volatile Map<String, List<FinanceResourceResponse>> data = Map.of();
    private volatile boolean initialized;

    public void load(Map<String, List<FinanceResourceResponse>> fetchedData) {
        if (initialized) {
            throw new IllegalStateException("Data store has already been initialized");
        }
        this.data = Collections.unmodifiableMap(fetchedData);
        this.initialized = true;
    }

    public List<FinanceResourceResponse> getData(String resourceType) {
        if (!initialized) {
            throw new DataNotLoadedException();
        }
        List<FinanceResourceResponse> result = data.get(resourceType);
        if (result == null) {
            throw new ResourceNotFoundException(resourceType);
        }
        return result;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
