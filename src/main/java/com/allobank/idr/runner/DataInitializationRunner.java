package com.allobank.idr.runner;

import com.allobank.idr.service.DataStoreService;
import com.allobank.idr.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializationRunner implements ApplicationRunner {
    
    private final List<IDRDataFetcher> dataFetchers;
    private final DataStoreService dataStoreService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting data initialization...");
        
        for (IDRDataFetcher fetcher : dataFetchers) {
            try {
                String resourceType = fetcher.getResourceType();
                log.info("Fetching data for resource: {}", resourceType);
                
                Map<String, Object> data = fetcher.fetchData();
                dataStoreService.storeData(resourceType, data);
                
                log.info("Successfully loaded data for: {}", resourceType);
            } catch (Exception e) {
                log.error("Failed to fetch data for resource: {}", fetcher.getResourceType(), e);
                throw new RuntimeException("Data initialization failed", e);
            }
        }
        
        dataStoreService.markAsInitialized();
        log.info("Data initialization completed successfully");
    }
}
