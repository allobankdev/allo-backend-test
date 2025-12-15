package com.allobank.frankfurter_aggregator.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.allobank.frankfurter_aggregator.service.strategy.DataFetcherStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceDataService {
    
    private final Map<String, DataFetcherStrategy> strategies;
    private final DataStorageService dataStorageService;
    
    public Object getFinanceData(String resourceType) {
        if (!dataStorageService.isLoaded()) {
            throw new IllegalStateException("Application is starting up. Please try again in a moment.");
        }
        
        Object data = dataStorageService.getData(resourceType);
        if (data == null) {
            throw new IllegalArgumentException("Resource type not found: " + resourceType);
        }
        
        return data;
    }
    
    public Map<String, DataFetcherStrategy> getStrategies() {
        return strategies;
    }
}
