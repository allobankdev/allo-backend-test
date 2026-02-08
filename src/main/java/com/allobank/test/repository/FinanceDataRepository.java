package com.allobank.test.repository;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FinanceDataRepository {

    private final Map<String, Object> dataStore = new ConcurrentHashMap<>();

    public void saveData(String key, Object data) {
        dataStore.put(key, data);
    }

    public Object getData(String key) {
        return dataStore.get(key);
    }

    public boolean isEmpty() {
        return dataStore.isEmpty();
    }
}