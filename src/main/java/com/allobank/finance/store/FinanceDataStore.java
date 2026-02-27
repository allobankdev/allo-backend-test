package com.allobank.finance.store;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinanceDataStore {

    private Map<String, Object> dataMap = new ConcurrentHashMap<>();

    public void setDataMap(Map<String, Object> newDataMap) {
        this.dataMap = Map.copyOf(newDataMap);
    }

    public Object getData(String resourceType) {
        return dataMap != null ? dataMap.get(resourceType) : null;
    }
}