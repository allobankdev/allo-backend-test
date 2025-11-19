package com.athallah.finance.service;

import com.athallah.finance.startegy.IDRDataFetcher;
import com.athallah.finance.util.constant.ResourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FinanceDataInitializer implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> strategyMap;
    private final FinanceDataStore dataStore;

    @Override
    public void run(ApplicationArguments args) {
        for (IDRDataFetcher fetcher : strategyMap.values()) {
            ResourceType type = fetcher.getResourceType();
            Object data = fetcher.fetchData();      // FETCH SEKALI SAJA DI STARTUP
            dataStore.put(type, data);
        }
    }
}
