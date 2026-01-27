package com.allo.finance.runner;

import com.allo.finance.strategy.IDRDataFetcher;
import com.allo.finance.store.FinanceDataStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FinanceDataLoaderRunner implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final FinanceDataStore dataStore;

    public FinanceDataLoaderRunner(List<IDRDataFetcher> fetchers,
                                   FinanceDataStore dataStore) {
        this.fetchers = fetchers;
        this.dataStore = dataStore;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, Object> loadedData = new HashMap<>();

        for (IDRDataFetcher fetcher : fetchers) {
            loadedData.put(fetcher.getResourceType(), fetcher.fetchData());
        }

        dataStore.initialize(loadedData);
    }
}
