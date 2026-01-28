package com.example.demo.startup;

import com.example.demo.registry.FetcherRegistry;
import com.example.demo.store.FinanceDataStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupDataLoader implements ApplicationRunner {

    private final FetcherRegistry registry;
    private final FinanceDataStore store;

    public StartupDataLoader(FetcherRegistry registry, FinanceDataStore store) {
        this.registry = registry;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {

        store.load("latest_idr_rates", registry.getFetcher("latest_idr_rates").fetchData());
        store.load("historical_idr_usd", registry.getFetcher("historical_idr_usd").fetchData());
        store.load("supported_currencies", registry.getFetcher("supported_currencies").fetchData());
    }
}
