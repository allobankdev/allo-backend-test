package com.allobank.runner;

import com.allobank.service.IDRDataFetcher;
import com.allobank.store.DataStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class DataInitializationRunner implements ApplicationRunner {

    private final List<IDRDataFetcher> strategies;
    private final DataStoreService dataStoreService;
    private final BeanFactory beanFactory;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("=== Starting Data Initialization ===");
        log.info("Found {} data fetching strategies", strategies.size());
        log.info("Factory --> {}", beanFactory.containsBean("frankfurterWebClientFactory"));
        try {
            for (IDRDataFetcher strategy : strategies) {
                String resourceType = strategy.getResourceType().getValue();
                log.info("Fetching data for: {}", resourceType);

                Object data = strategy.fetchFromExternalApi();
                dataStoreService.storeData(strategy.getResourceType(), data);
                log.info("Successfully loaded data for: {}", resourceType);
            }

            dataStoreService.markInitialized();
            log.info("=== Data Initialization Complete ===");
            log.info("Application ready to serve requests");

        } catch (Exception e) {
            log.error("=== Data Initialization Failed ===", e);
            throw new RuntimeException("Failed to initialize application data", e);
        }
    }
}
