package com.allo.bank.service.store;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import com.allo.bank.dto.FinanceDataItem;
import com.allo.bank.exception.DataNotInitializedException;

@Component
public class InMemoryFinanceDataStore {

    private final AtomicReference<Map<String, List<FinanceDataItem>>> store = new AtomicReference<>(Map.of());

    public void replaceAll(Map<String, List<FinanceDataItem>> data) {
        store.set(Map.copyOf(data));
    }

    public List<FinanceDataItem> getByResourceType(String resourceType) {
        List<FinanceDataItem> data = store.get().get(resourceType);
        if (data == null) {
            throw new DataNotInitializedException(resourceType);
        }
        return data;
    }

    public boolean hasData(String resourceType) {
        return store.get().containsKey(resourceType);
    }
}
