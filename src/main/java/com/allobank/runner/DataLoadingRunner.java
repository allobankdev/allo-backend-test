package com.allobank.runner;

import com.allobank.service.DataFetchingService;
import com.allobank.service.DataStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Startup data runner that loads all IDR-related data from the Frankfurter API
 * exactly once when the application starts.
 * 
 * Benefits of ApplicationRunner over @PostConstruct:
 * - Executes after the entire application context is fully initialized
 * - Has access to application arguments and environment
 * - Guarantees all beans are ready and autowired
 * - Better error handling and reporting
 * - Can exit with specific error codes if needed
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoadingRunner implements ApplicationRunner {
    
    private final DataFetchingService dataFetchingService;
    private final DataStore dataStore;
    
    @Override
    public void run(org.springframework.boot.ApplicationArguments args) throws Exception {
        log.info("Starting data loading on application startup...");
        
        try {
            // Initialize the strategy map
            dataFetchingService.initializeStrategyMap();
            
            // Fetch all data from external APIs
            int loadedCount = dataFetchingService.fetchAllData();
            
            // Mark the store as initialized (prevents further modifications)
            dataStore.markAsInitialized();
            
            log.info("Data loading completed successfully. {} resources loaded.", loadedCount);
            
            if (loadedCount == 0) {
                log.warn("No resources were loaded during startup. This may indicate a configuration issue.");
            }
            
        } catch (Exception e) {
            log.error("Fatal error during application startup data loading", e);
            throw new RuntimeException("Failed to load critical startup data", e);
        }
    }
}
