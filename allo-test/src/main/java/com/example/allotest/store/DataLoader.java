package com.example.allotest.store;

import com.example.allotest.strategy.IDataFetcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataLoader implements ApplicationRunner {
    private final Map<String, IDataFetcher> strategies;
    private final DataStore store;

    public DataLoader(Map<String, IDataFetcher> strategies, DataStore store) {
        this.strategies = strategies;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        strategies.forEach((key, strategy) -> {
            Object[] data = strategy.fetchData();
            store.putIntoStore(key, data);
        });
        store.makeLoaded();
    }
}
