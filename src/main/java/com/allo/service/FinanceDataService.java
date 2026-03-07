package com.allo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.allo.dto.FinanceResourceResponse;
import com.allo.store.FinanceDataStore;
import com.allo.strategy.HistoricalIDRUSDFetcher;

@Service
public class FinanceDataService {

    private final FinanceDataStore dataStore;
    private final HistoricalIDRUSDFetcher historicalFetcher;

    public FinanceDataService(FinanceDataStore dataStore, HistoricalIDRUSDFetcher historicalFetcher) {
        this.dataStore = dataStore;
        this.historicalFetcher = historicalFetcher;
    }

    public List<FinanceResourceResponse> getDataByResourceType(
            String resourceType, String startDate, String endDate) {
        if ("historical_idr_usd".equals(resourceType) && startDate != null && endDate != null) {
            return historicalFetcher.fetchByRange(startDate, endDate);
        }
        return dataStore.getData(resourceType);
    }
}
