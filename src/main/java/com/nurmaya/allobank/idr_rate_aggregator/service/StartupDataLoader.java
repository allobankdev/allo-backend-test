package com.nurmaya.allobank.idr_rate_aggregator.service;

import java.util.List;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.nurmaya.allobank.idr_rate_aggregator.strategy.IDRDataFetcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StartupDataLoader implements ApplicationRunner{
    private final Map<String, IDRDataFetcher> fetchers;
    private final AggregatedDataStore dataStore;

    public StartupDataLoader(Map<String, IDRDataFetcher> fetchers,
                             AggregatedDataStore dataStore) {
        this.fetchers = fetchers;
        this.dataStore = dataStore;
    }

    @Override
    public void run(ApplicationArguments args) {
        fetchers.forEach((key, fetcher) -> {
            log.info("Fetching data for resourceType = {}", key);
            List<?> data = fetcher.fetchData();
            dataStore.putData(key, data);
            log.info("Data loaded for {}", key);
        });
    }
}
