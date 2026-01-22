package com.allobank.idr_rate_aggregator.runner;

import com.allobank.idr_rate_aggregator.service.DataCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * ApplicationRunner implementation for loading all data at startup.
 * 
 * This component:
 * 1. Runs automatically after the Spring context is initialized
 * 2. Loads all data once before the application accepts requests
 * 3. Ensures data is available in cache before endpoint access
 * 
 * Benefits of ApplicationRunner over @PostConstruct:
 * - Runs after the entire application context is fully initialized
 * - Access to application arguments if needed
 * - Better control over execution order with @Order
 * - Clear separation of initialization logic from bean lifecycle
 * - More suitable for complex initialization that may interact with multiple beans
 */
@Component
@Order(1) // Ensures this runs early in the startup sequence
@Slf4j
public class DataLoaderRunner implements ApplicationRunner {

    private final DataCacheService dataCacheService;

    @Autowired
    public DataLoaderRunner(DataCacheService dataCacheService) {
        this.dataCacheService = dataCacheService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("========================================");
        log.info("Starting data initialization process...");
        log.info("========================================");

        try {
            long startTime = System.currentTimeMillis();
            
            // Load all data from external API
            dataCacheService.loadAllData();
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info("========================================");
            log.info("Data initialization completed successfully in {} ms", duration);
            log.info("Loaded resource types: {}", dataCacheService.getSupportedResourceTypes());
            log.info("Application is ready to serve requests");
            log.info("========================================");
            
        } catch (Exception e) {
            log.error("========================================");
            log.error("FATAL: Data initialization failed!", e);
            log.error("========================================");
            
            // Re-throw to prevent application startup with incomplete data
            throw new RuntimeException("Application startup failed: Unable to load required data", e);
        }
    }
}
