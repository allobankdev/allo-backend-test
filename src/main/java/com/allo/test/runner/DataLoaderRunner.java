package com.allo.test.runner;

import com.allo.test.service.InMemoryDataStore;
import com.allo.test.strategy.IDRDataFetcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoaderRunner implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final InMemoryDataStore dataStore;

    public DataLoaderRunner(List<IDRDataFetcher> fetchers,
                            InMemoryDataStore dataStore) {
        this.fetchers = fetchers;
        this.dataStore = dataStore;
    }

    @Override
    public void run(ApplicationArguments args) {

        for (IDRDataFetcher fetcher : fetchers) {
            String key = fetcher.getResourceType();
            List<Object> data = fetcher.fetchData();

            dataStore.put(key, data);

            System.out.println("Loaded data for: " + key);
        }
    }
}
