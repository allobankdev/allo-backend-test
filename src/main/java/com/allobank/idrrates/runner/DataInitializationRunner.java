package com.allobank.idrrates.runner;

import com.allobank.idrrates.service.DataStoreService;
import com.allobank.idrrates.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataInitializationRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializationRunner.class);

    private final List<IDRDataFetcher> fetchers;
    private final DataStoreService dataStoreService;

    public DataInitializationRunner(List<IDRDataFetcher> fetchers, DataStoreService dataStoreService) {
        this.fetchers = fetchers;
        this.dataStoreService = dataStoreService;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting data initialization with {} fetchers", fetchers.size());

        Map<String, List<?>> data = new HashMap<>();

        for (IDRDataFetcher fetcher : fetchers) {
            log.info("Fetching data for resource type: {}", fetcher.getResourceType());
            List<?> result = fetcher.fetchAndTransform();
            data.put(fetcher.getResourceType(), result);
            log.info("Successfully fetched {} items for {}", result.size(), fetcher.getResourceType());
        }

        dataStoreService.initialize(data);
        log.info("Data initialization complete. Store contains {} resource types.", data.size());
    }
}
