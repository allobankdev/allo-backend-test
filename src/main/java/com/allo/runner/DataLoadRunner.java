package com.allo.runner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.allo.dto.FinanceResourceResponse;
import com.allo.store.FinanceDataStore;
import com.allo.strategy.IDRFetcher;

@Component
public class DataLoadRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoadRunner.class);

    private final Map<String, IDRFetcher> fetcherMap;
    private final FinanceDataStore dataStore;

    public DataLoadRunner(Map<String, IDRFetcher> fetcherMap, FinanceDataStore dataStore) {
        this.fetcherMap = fetcherMap;
        this.dataStore = dataStore;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting data load from Frankfurter API for {} resource(s)...", fetcherMap.size());

        Map<String, List<FinanceResourceResponse>> aggregated = new HashMap<>();

        for (Map.Entry<String, IDRFetcher> entry : fetcherMap.entrySet()) {
            IDRFetcher fetcher = entry.getValue();
            String type = fetcher.resourceType();
            try {
                log.info("Fetching resource: {}", type);
                List<FinanceResourceResponse> data = fetcher.fetch();
                aggregated.put(type, data);
                log.info("Successfully loaded resource: {} ({} record(s))", type, data.size());
            } catch (Exception ex) {
                log.error("Failed to load resource '{}': {}", type, ex.getMessage(), ex);
                aggregated.put(type, List.of());
            }
        }

        dataStore.load(aggregated);
        log.info("Data load complete. {} resource type(s) available.", aggregated.size());
    }
}
