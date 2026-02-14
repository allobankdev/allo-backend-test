package com.vii.idragregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Luthfi Aryarizki
 * @date Created on 2026/02/14 at 09:00 p.m
 */
@Slf4j
@Service
public class FinanceDataService {

    private Map<String, Object> storage = new ConcurrentHashMap<>();

    public void initializeData(String key, Object data) {
        if (data != null) {
            this.storage.put(key, data);
            log.info("Data for {} successfully cached.", key);
        }
    }

    public Object getData(String key) {
        return storage.get(key);
    }

    public void lockStorage() {
        this.storage = Collections.unmodifiableMap(new java.util.HashMap<>(storage));
        log.info("In-memory storage is now locked and immutable.");
    }

}
