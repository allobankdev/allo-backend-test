package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.strategy.IdrDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataPreloadRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataPreloadRunner.class);

    private final List<IdrDataFetcher> fetchers;
    private final InMemoryFinanceStore store;

    public DataPreloadRunner(List<IdrDataFetcher> fetchers, InMemoryFinanceStore store) {
        this.fetchers = fetchers;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, Object> loaded = new HashMap<>();

        for (IdrDataFetcher fetcher : fetchers) {
            String type = fetcher.resourceType();
            log.info("Preloading data for resourceType={}", type);

            Object value = fetcher.fetchFromApi();
            loaded.put(type, value);
        }

        store.initialize(loaded);
        log.info("All Frankfurter data preloaded into in-memory store");
    }
}
