package com.allobank.finance.runner;

import com.allobank.finance.service.fetcher.IDRDataFetcher;
import com.allobank.finance.store.FinanceDataStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class FinanceDataLoader implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> fetcherMap;
    private final FinanceDataStore dataStore;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting preloading of finance data...");

        Map<String, Object> tempMap = new HashMap<>();

        fetcherMap.forEach((resourceType, fetcher) -> {
            try {
                Object data = fetcher.fetchData();
                tempMap.put(resourceType, data);
                log.info("Preloaded records for resource type '{}'", resourceType);
            } catch (Exception e) {
                log.error("Failed to load data for '{}': {}", resourceType, e.getMessage(), e);
            }
        });

        dataStore.setDataMap(tempMap);

        log.info("Finance data preloading completed!");
    }
}