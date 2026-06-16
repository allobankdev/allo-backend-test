package com.frankfurter.aggregator.service;
import com.frankfurter.aggregator.dto.internal.FinanceDataResponse;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataStorageService {
    private final Map<String, FinanceDataResponse> dataStore = new ConcurrentHashMap<>();

    public void storeData(String resourceType, FinanceDataResponse data) {
        dataStore.put(resourceType, data);
    }

    public FinanceDataResponse getData(String resourceType) {
        return dataStore.get(resourceType);
    }

    public Map<String, FinanceDataResponse> getAllData() {
        return Collections.unmodifiableMap(dataStore);
    }
}
