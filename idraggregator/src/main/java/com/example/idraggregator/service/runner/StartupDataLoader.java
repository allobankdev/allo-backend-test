package com.example.idraggregator.service.runner;

import com.example.idraggregator.service.DataStoreService;
import com.example.idraggregator.service.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Loads all three resources exactly once on application startup.
 */
@Component
public class StartupDataLoader implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(StartupDataLoader.class);

    private final Map<String, IDRDataFetcher<?>> fetchers;
    private final DataStoreService dataStoreService;

    public StartupDataLoader(Map<String, IDRDataFetcher<?>> fetchers, DataStoreService dataStoreService) {
        this.fetchers = fetchers;
        this.dataStoreService = dataStoreService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("StartupDataLoader: loading resources from Frankfurter API...");
        Map<String, Object> loaded = new HashMap<>();
        for (Map.Entry<String, IDRDataFetcher<?>> entry : fetchers.entrySet()) {
            String key = entry.getKey();
            try {
                IDRDataFetcher<?> fetcher = entry.getValue();
                Object payload = fetcher.fetch();
                loaded.put(fetcher.resourceKey(), payload);
                log.info("Loaded resource: {}", fetcher.resourceKey());
            } catch (Exception e) {
                log.error("Failed to load resource {} : {}", key, e.getMessage(), e);
                // propagate to fail fast (test requires that data be loaded on startup)
                throw new IllegalStateException("Failed to load startup data for " + key, e);
            }
        }
        dataStoreService.setAll(loaded);
        log.info("All resources loaded and stored immutably.");
    }
}
