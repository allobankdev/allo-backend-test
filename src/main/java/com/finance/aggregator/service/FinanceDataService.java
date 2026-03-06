package com.finance.aggregator.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.finance.aggregator.strategy.IDRDataFetcher;

@Service
public class FinanceDataService {
    private Map<String, Object> immutableCache = new ConcurrentHashMap<>();

    public void refreshCache(List<IDRDataFetcher> strategies, WebClient client) {
        Map<String, Object> data = new HashMap<>();
        for (IDRDataFetcher strategy : strategies) {
            try {
                data.put(strategy.getResourceType(), strategy.fetchData(client));
            } catch (Exception e) {
                data.put(strategy.getResourceType(), "Error fetching data: " + e.getMessage());
            }
        }
        this.immutableCache = Collections.unmodifiableMap(data);
    }

    public Object getData(String type) {
        return immutableCache.get(type);
    }
}

