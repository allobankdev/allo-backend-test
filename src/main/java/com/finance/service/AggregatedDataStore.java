package com.finance.service;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AggregatedDataStore {
    private Map<String, List<?>> data;

    public void initialize(Map<String, List<?>> data) {
        this.data = data;
    }

    public List<?> get(String key) {
        return data.get(key);
    }
}
