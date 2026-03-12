package com.allobank.finace.runner;

import com.allobank.finace.service.DataStoreService;
import com.allobank.finace.service.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DataStartupRunner implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> fetchers;
    private final DataStoreService dataStoreService;
    private final WebClient webClient;

    @Autowired
    public DataStartupRunner(Map<String, IDRDataFetcher> fetchers,
                             DataStoreService dataStoreService,
                             WebClient webClient) {
        this.fetchers = fetchers;
        this.dataStoreService = dataStoreService;
        this.webClient = webClient;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting data ingestion for all resources...");

        fetchers.forEach((resourceType, fetcher) -> {
            try {
                log.info("Fetching resource: {}", resourceType);
                List<Map<String, Object>> data = fetcher.fetch(webClient);
                dataStoreService.store(resourceType, data);
            } catch (WebClientResponseException e) {
                log.error("HTTP error fetching resource '{}': status={}, body={}",
                        resourceType, e.getStatusCode(), e.getResponseBodyAsString());
                dataStoreService.store(resourceType, List.of());
            } catch (Exception e) {
                log.error("Unexpected error fetching resource '{}': {}", resourceType, e.getMessage(), e);
                dataStoreService.store(resourceType, List.of());
            }
        });

        dataStoreService.markInitialized();
        log.info("Data ingestion complete. In-memory store is now locked.");
    }
}
