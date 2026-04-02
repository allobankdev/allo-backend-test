package com.allobank.frankfurter.runner;

import com.allobank.frankfurter.service.InMemoryDataStore;
import com.allobank.frankfurter.service.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final List<IDRDataFetcher> fetchers;
    private final InMemoryDataStore dataStore;

    public DataLoader(List<IDRDataFetcher> fetchers, InMemoryDataStore dataStore) {
        this.fetchers = fetchers;
        this.dataStore = dataStore;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Loading data from Frankfurter API...");
        for (IDRDataFetcher fetcher : fetchers) {
            try {
                var result = fetcher.fetchData();
                dataStore.put(result);
                logger.info("Loaded data for resource: {}", result.getResourceType());
            } catch (Exception e) {
                logger.error("Failed to load data for resource: {}", fetcher.getResourceType(), e);
                // Rethrow to prevent app from starting with incomplete data.
                throw new RuntimeException("Data loading failed", e);
            }
        }
        logger.info("Data loading completed.");
    }
}