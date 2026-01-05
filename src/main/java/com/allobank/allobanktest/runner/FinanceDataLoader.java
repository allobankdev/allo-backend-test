package com.allobank.allobanktest.runner;

import com.allobank.allobanktest.store.FinanceDataStore;
import com.allobank.allobanktest.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FinanceDataLoader implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final FinanceDataStore store;

    public FinanceDataLoader(List<IDRDataFetcher> fetchers,  FinanceDataStore store) {
        this.fetchers = fetchers;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting finance data initialization");

        for (IDRDataFetcher fetcher : fetchers) {
            String resourceType = fetcher.getResourceType();
            log.info("Initializing resource: {}", resourceType);

            try {
                Object fetched = fetcher.fetchAndTransform();
                store.put(resourceType, fetched);
                log.info("Successfully initialized resource: {}", resourceType);
            } catch (Exception ex) {
                log.error("Failed to initialize resource: {}", resourceType, ex);
                throw ex; // fail fast → startup should fail if data invalid
            }
        }
        store.lock();

        log.info("Finance data initialization completed");
    }
}
