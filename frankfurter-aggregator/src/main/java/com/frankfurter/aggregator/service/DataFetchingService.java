package com.frankfurter.aggregator.service;

import com.frankfurter.aggregator.dto.internal.FinanceDataResponse;
import com.frankfurter.aggregator.strategy.IDRDataFetcher;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class DataFetchingService {

    private final Map<String, IDRDataFetcher> strategies;

    public DataFetchingService(Map<String, IDRDataFetcher> strategies) {
        this.strategies = strategies;
    }

    public void fetchAndStoreAllData(DataStorageService storageService) {
        for (IDRDataFetcher fetcher : strategies.values()) {
            FinanceDataResponse response = fetcher.fetchData();
            if (response != null) {
                storageService.storeData(fetcher.getResourceType(), response);
            }
        }
    }
}
