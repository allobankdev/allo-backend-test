package com.allo.backend.test.code.service.startup;

import com.allo.backend.test.code.service.DataStorageService;
import com.allo.backend.test.code.service.strategy.DataFetcherStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * ApplicationRunner that loads all financial data on application startup.
 * Runs with high priority (Order 1) to ensure data is available before serving requests.
 */
@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class DataInitializationRunner implements ApplicationRunner {

    private final WebClient webClient;
    private final List<DataFetcherStrategy> strategies;
    private final DataStorageService dataStorageService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting data initialization with {} strategies", strategies.size());

        try {
            // Fetch data from all strategies
            for (DataFetcherStrategy strategy : strategies) {
                String resourceType = strategy.getResourceType();
                log.info("Fetching data for resource type: {}", resourceType);

                Object data = strategy.fetchData(webClient);
                dataStorageService.storeData(resourceType, data);

                log.info("Successfully loaded data for resource type: {}", resourceType);
            }

            // Mark data store as initialized and immutable
            dataStorageService.markAsInitialized();

            log.info("Data initialization completed successfully. All resources loaded and immutable.");

        } catch (Exception e) {
            log.error("Failed to initialize data on startup", e);
            throw new RuntimeException("Application startup failed due to data initialization error", e);
        }
    }
}
