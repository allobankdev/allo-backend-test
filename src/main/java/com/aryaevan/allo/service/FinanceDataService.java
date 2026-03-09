package com.aryaevan.allo.service;

import com.aryaevan.allo.strategy.IDRDataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * Service layer for finance data aggregation.
 * Orchestrates strategy execution and data retrieval.
 */
@Service
public class FinanceDataService {
    
    private final Map<String, IDRDataFetcher> strategyMap;

    @Autowired
    public FinanceDataService(Map<String, IDRDataFetcher> strategyMap) {
        this.strategyMap = strategyMap;
    }

    /**
     * Retrieves data for the specified resource type using the appropriate strategy.
     * 
     * @param resourceType The type of resource to fetch
     * @return The data retrieved by the strategy
     * @throws IllegalArgumentException if resource type is invalid
     */
    public Object getFinanceData(String resourceType) {
        IDRDataFetcher fetcher = strategyMap.get(resourceType);
        
        if (fetcher == null) {
            throw new IllegalArgumentException("Invalid resource type: " + resourceType);
        }
        
        return fetcher.fetchData();
    }
}
