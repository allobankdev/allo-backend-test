package com.allobank.finance.registry;

import com.allobank.finance.exception.ErrorCode;
import com.allobank.finance.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class IDRDataFetcherRegistry {

    private final Map<String, IDRDataFetcher> dataFetcherMap = new ConcurrentHashMap<>();

    public IDRDataFetcherRegistry(List<IDRDataFetcher> dataFetchers) {
        log.info("Registering {} data fetchers", dataFetchers.size());
        dataFetchers.forEach(fetcher -> dataFetcherMap.putIfAbsent(fetcher.getResourceType(), fetcher));
    }

    public IDRDataFetcher get(String resourceType) {
        IDRDataFetcher dataFetcher = dataFetcherMap.get(resourceType);

        if (dataFetcher == null) {
            throw ErrorCode.RESOURCE_NOT_FOUND.toException("Data fetcher for resource type " + resourceType + " not found.");
        }

        return dataFetcher;
    }
    
    public Map<String, IDRDataFetcher> getAll() {
        return dataFetcherMap;
    }
}
