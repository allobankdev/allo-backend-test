package com.allobank.runner;

import com.allobank.service.InMemoryDataStore;
import com.allobank.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * ApplicationRunner that fetches all data from external API on startup
 * and loads it into the in-memory store.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializationRunner implements ApplicationRunner {

    private final List<IDRDataFetcher> dataFetchers;
    private final InMemoryDataStore dataStore;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting data initialization for {} resource types", dataFetchers.size());
        
        CountDownLatch latch = new CountDownLatch(dataFetchers.size());
        java.util.concurrent.atomic.AtomicInteger successCount = new java.util.concurrent.atomic.AtomicInteger(0);
        java.util.concurrent.atomic.AtomicInteger failureCount = new java.util.concurrent.atomic.AtomicInteger(0);

        for (IDRDataFetcher fetcher : dataFetchers) {
            String resourceType = fetcher.getResourceType();
            
            fetcher.fetchData()
                    .doOnSuccess(data -> {
                        if (data != null) {
                            dataStore.storeData(resourceType, data);
                            log.info("Successfully loaded data for resource: {}", resourceType);
                            successCount.incrementAndGet();
                        } else {
                            log.warn("Received null data for resource: {}", resourceType);
                            failureCount.incrementAndGet();
                        }
                        latch.countDown();
                    })
                    .doOnError(error -> {
                        log.error("Failed to load data for resource: {}", resourceType, error);
                        failureCount.incrementAndGet();
                        latch.countDown();
                    })
                    .subscribe();
        }

        try {
            // Wait for all fetchers to complete (with timeout)
            boolean completed = latch.await(30, TimeUnit.SECONDS);
            if (!completed) {
                log.error("Data initialization timed out after 30 seconds");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Data initialization was interrupted", e);
        }

        // Mark data loading as complete
        dataStore.markDataLoaded();

        log.info("Data initialization completed. Success: {}, Failures: {}", 
                successCount.get(), failureCount.get());
        
        if (failureCount.get() > 0) {
            log.warn("Some resources failed to load. Application will continue but some endpoints may not work.");
        }
    }
}

