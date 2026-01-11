package com.prasetyahs.allo.finance.runner;

import com.prasetyahs.allo.finance.store.InMemoryDataStore;
import com.prasetyahs.allo.finance.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StartupDataRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupDataRunner.class);

    private final List<IDRDataFetcher> fetchers;
    private final InMemoryDataStore store;
    private final WebClient webClient;

    public StartupDataRunner(List<IDRDataFetcher> fetchers, InMemoryDataStore store, WebClient webClient) {
        this.fetchers = fetchers;
        this.store = store;
        this.webClient = webClient;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting initial data fetch...");
        Map<String, Object> aggregatedData = new HashMap<>();

        for (IDRDataFetcher fetcher : fetchers) {
            try {
                log.info("Fetching resource: {}", fetcher.getResourceType());
                Object data = fetcher.fetchAndProcess(webClient);
                aggregatedData.put(fetcher.getResourceType(), data);
            } catch (Exception e) {
                log.error("Failed to fetch resource: {}", fetcher.getResourceType(), e);
                // Graceful handling: Could put an error object or null.
                // We choose to omit the key so the controller knows it's missing.
            }
        }

        store.initialize(aggregatedData);
        log.info("Data initialization complete.");
    }
}
