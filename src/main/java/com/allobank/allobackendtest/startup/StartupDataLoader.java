package com.allobank.allobackendtest.startup;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.allobank.allobackendtest.store.InMemoryDataStore;
import com.allobank.allobackendtest.strategy.IDRDataFetcher;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StartupDataLoader implements CommandLineRunner {

    private final List<IDRDataFetcher> fetchers;
    private final InMemoryDataStore dataStore;

    public StartupDataLoader(List<IDRDataFetcher> fetchers,InMemoryDataStore dataStore) {
        this.fetchers = fetchers;
        this.dataStore = dataStore;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting IDR data preload process...");
        for (IDRDataFetcher fetcher : fetchers) {
            String resourceType = fetcher.getResourceType();

            try {
                log.info("Loading data for resourceType={}", resourceType);
                Object data = fetcher.fetchData();
                dataStore.put(resourceType, data);

                log.info("Successfully loaded data for resourceType={}", resourceType);

            } catch (Exception ex) {
                log.error("Failed to load data for resourceType={}", resourceType, ex);
            }
        }

        log.info("IDR data preload process completed");
    }

}
