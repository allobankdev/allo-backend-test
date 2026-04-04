package com.allobank.allobank_api.runner;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import com.allobank.allobank_api.store.DataStore;
import com.allobank.allobank_api.strategy.IDRDataFetcher;

public class DataLoaderRunner implements ApplicationRunner {
    private final List<IDRDataFetcher<?>> fetchers;
    private final DataStore store;

    public DataLoaderRunner(List<IDRDataFetcher<?>> fetchers, DataStore store) {
        this.fetchers = fetchers;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        for (IDRDataFetcher<?> fetcher : fetchers) {
            Object data = fetcher.fetchAndTransform();
            store.put(fetcher.getType(), data);
        }
    }
    
}
