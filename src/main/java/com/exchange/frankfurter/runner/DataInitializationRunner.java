package com.exchange.frankfurter.runner;

import com.exchange.frankfurter.store.FinanceDataStore;
import com.exchange.frankfurter.strategy.IDRDataFetcher;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataInitializationRunner implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final FinanceDataStore store;

    public DataInitializationRunner(List<IDRDataFetcher> fetchers,
                                    FinanceDataStore store) {
        this.fetchers = fetchers;
        this.store = store;
    }

    @Override
    public void run(org.springframework.boot.ApplicationArguments args) {
        Map<String, Object> tempStore = new HashMap<>();

        for (IDRDataFetcher fetcher : fetchers) {
            Object data = fetcher.fetchData();
            tempStore.put(fetcher.getResourceType(), data);
        }

        store.initialize(tempStore);

        System.out.println("All finance data loaded into memory.");
    }
}
