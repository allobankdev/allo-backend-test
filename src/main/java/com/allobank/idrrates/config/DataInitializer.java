package com.allobank.idrrates.config;

import com.allobank.idrrates.store.DataStore;
import com.allobank.idrrates.strategy.IdrDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private DataStore dataStore;
    private final List<IdrDataFetcher> fetchers;

    public DataInitializer(List<IdrDataFetcher> fetchers) {
        this.fetchers = fetchers;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting data initialization...");
        for (IdrDataFetcher fetcher : fetchers) {
            try {
                log.info("Fetching data for resource type: {}", fetcher.getResourceType());
                Object data = fetcher.fetchData();
                dataStore.put(fetcher.getResourceType(), data);
            } catch (Exception e) {
                log.error("Failed to fetch data for resource type: {} - {}", fetcher.getResourceType(), e.getMessage());
            }
        }
        dataStore.markInitialized();
        log.info("Data initialization complete");
    }
}
