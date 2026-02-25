package com.allobank.financeaggregator.service;

import com.allobank.financeaggregator.exception.DataNotLoadedException;
import com.allobank.financeaggregator.exception.ResourceNotFoundException;
import com.allobank.financeaggregator.model.FinanceDataItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Component;

@Component
public class FinanceDataStore {

    private final AtomicReference<Map<String, List<FinanceDataItem<?>>>> dataRef = new AtomicReference<>();

    public void load(Map<String, List<FinanceDataItem<?>>> data) {
        Map<String, List<FinanceDataItem<?>>> immutable = new HashMap<>();
        for (Map.Entry<String, List<FinanceDataItem<?>>> entry : data.entrySet()) {
            immutable.put(entry.getKey(), List.copyOf(entry.getValue()));
        }
        boolean updated = dataRef.compareAndSet(null, Map.copyOf(immutable));
        if (!updated) {
            throw new IllegalStateException("Finance data already loaded");
        }
    }

    public List<FinanceDataItem<?>> get(String resourceType) {
        Map<String, List<FinanceDataItem<?>>> data = dataRef.get();
        if (data == null) {
            throw new DataNotLoadedException("Finance data is not loaded yet");
        }
        List<FinanceDataItem<?>> items = data.get(resourceType);
        if (items == null) {
            throw new ResourceNotFoundException("Unknown resourceType: " + resourceType);
        }
        return items;
    }

    public boolean isLoaded() {
        return dataRef.get() != null;
    }
}
