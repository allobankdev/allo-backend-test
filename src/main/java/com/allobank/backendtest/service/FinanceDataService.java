package com.allobank.backendtest.service;

import com.allobank.backendtest.constant.MessageConstants;
import com.allobank.backendtest.exception.DataNotReadyException;
import com.allobank.backendtest.exception.ResourceNotFoundException;
import com.allobank.backendtest.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FinanceDataService {
    private final Map<String, IDRDataFetcher> strategyMap;
    private volatile Map<String, Object> dataStore;

    public FinanceDataService(List<IDRDataFetcher> fetchers) {
        this.strategyMap = fetchers.stream().collect(Collectors.toUnmodifiableMap(IDRDataFetcher::getResourceType, Function.identity()));
    }

    public void loadAllData() {
        log.info(MessageConstants.LOG_APP_STARTUP_LOADING);
        Map<String, Object> data = new HashMap<>();

        for (Map.Entry<String, IDRDataFetcher> entry : strategyMap.entrySet()) {
            String resourceType = entry.getKey();
            
            log.info(MessageConstants.LOG_FETCH_START, resourceType);
            Object result = entry.getValue().fetchData();
            
            data.put(resourceType, result);
            log.info(MessageConstants.LOG_FETCH_SUCCESS, resourceType);
        }

        this.dataStore = Collections.unmodifiableMap(data);
    }

    public Object getData(String resourceType) {
        if (dataStore == null) {
            throw new DataNotReadyException(MessageConstants.ERROR_DATA_NOT_READY);
        }

        if (!dataStore.containsKey(resourceType)) {
            throw new ResourceNotFoundException(MessageConstants.ERROR_RESOURCE_NOT_FOUND + resourceType);
        }

        return dataStore.get(resourceType);
    }

    public boolean isDataLoaded() {
        return dataStore != null;
    }
}
