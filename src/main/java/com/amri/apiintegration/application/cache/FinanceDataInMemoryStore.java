package com.amri.apiintegration.application.cache;

import com.amri.apiintegration.dto.frankfurter.FinanceResourceResultDto;
import com.amri.apiintegration.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class FinanceDataInMemoryStore {

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private volatile Map<String, FinanceResourceResultDto> resourceData = Map.of();

    public synchronized void initialize(Map<String, FinanceResourceResultDto> loadedData) {
        if (!initialized.compareAndSet(false, true)) {
            throw new IllegalStateException("Finance data has already been initialized");
        }
        this.resourceData = Map.copyOf(loadedData);
    }

    public FinanceResourceResultDto getByResourceType(String resourceType) {
        FinanceResourceResultDto value = resourceData.get(resourceType);
        if (value == null) {
            throw new ResourceNotFoundException("Unsupported resourceType: " + resourceType);
        }
        return value;
    }

    public Map<String, FinanceResourceResultDto> snapshot() {
        return resourceData;
    }
}
