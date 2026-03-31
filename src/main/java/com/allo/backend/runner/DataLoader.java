package com.allo.backend.runner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.allo.backend.store.DataStore;
import com.allo.backend.strategy.IDRDataFetcher;

@Component
public class DataLoader implements ApplicationRunner {
    private final List<IDRDataFetcher> fetchers;
    private final DataStore store;

    public DataLoader(List<IDRDataFetcher> fetchers, DataStore store) {
        this.fetchers = fetchers;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, Object> result = new HashMap<>();

        for (IDRDataFetcher f : fetchers) {
            result.put(f.getType(), f.fetchData());
        }

        store.setData(result);
    }
}
