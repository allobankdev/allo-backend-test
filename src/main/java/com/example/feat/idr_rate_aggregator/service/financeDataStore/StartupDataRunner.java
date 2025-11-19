package com.example.feat.idr_rate_aggregator.service.financeDataStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class StartupDataRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(StartupDataRunner.class);

    private final List<IDRDataFetcher> fetchers;
    private final DataStoreService dataStoreService;

    public StartupDataRunner(List<IDRDataFetcher> fetchers, DataStoreService dataStoreService) {
        this.fetchers = fetchers;
        this.dataStoreService = dataStoreService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting initial data load from Frankfurter API...");
        try {
            for (IDRDataFetcher fetcher : fetchers) {
                log.info("Fetching resource: {}", fetcher.getResourceKey());
                Object data = fetcher.fetchData();
                dataStoreService.storeData(fetcher.getResourceKey(), data);
            }
            dataStoreService.finalizeStore();
            log.info("All initial data loaded successfully and store finalized (Immutable).");
        } catch (Exception e) {
            log.error("Failed to load initial data from Frankfurter API.", e);
            throw new RuntimeException("Fatal: Application failed to initialize data.", e);
        }
    }
}
