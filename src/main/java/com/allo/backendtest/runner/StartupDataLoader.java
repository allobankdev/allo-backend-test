package com.allo.backendtest.runner;


import com.allo.backendtest.store.FinanceDataStore;
import com.allo.backendtest.strategy.IDRDataFetcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StartupDataLoader implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> fetchers;
    private final FinanceDataStore store;

    public StartupDataLoader(Map<String, IDRDataFetcher> fetchers,
                             FinanceDataStore store) {
        this.fetchers = fetchers;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {

        fetchers.forEach((key, fetcher) ->
                store.put(key, fetcher.fetchAndTransform())
        );

        store.finalizeSnapshot();
    }
}
