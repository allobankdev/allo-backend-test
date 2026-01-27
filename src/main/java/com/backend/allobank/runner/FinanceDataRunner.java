package com.backend.allobank.runner;

import com.backend.allobank.store.InMemoryFinanceStore;
import com.backend.allobank.strategy.IDRDataFetcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FinanceDataRunner implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final InMemoryFinanceStore store;

    public FinanceDataRunner(List<IDRDataFetcher> fetchers, InMemoryFinanceStore store) {
        this.fetchers = fetchers;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {

        Map<String, Object> aggregated = new HashMap<>();

        for (IDRDataFetcher fetcher : fetchers) {
            String key = fetcher.getResourceType();

            if (key == null) {
                throw new IllegalStateException("Fetcher returned null resourceType: " + fetcher.getClass());
            }

            try {
                Object data = fetcher.fetchAndTransform();
                aggregated.put(key, data);
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Failed to initialize resource: " + key, e
                );
            }
        }

        store.initialize(aggregated);

        System.out.println("Finance data initialized: " + aggregated.keySet());
    }
}
