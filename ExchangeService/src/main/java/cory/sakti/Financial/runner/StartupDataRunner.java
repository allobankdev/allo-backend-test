package cory.sakti.Financial.runner;

import cory.sakti.Financial.service.InMemoryDataStoreService;
import cory.sakti.Financial.strategy.FinancialDataStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class StartupDataRunner implements ApplicationRunner {

    private final List<FinancialDataStrategy> strategies;
    private final InMemoryDataStoreService dataStore;
    private final RestTemplate restTemplate;

    public StartupDataRunner(List<FinancialDataStrategy> strategies,
                             InMemoryDataStoreService dataStore,
                             RestTemplate restTemplate) {
        this.strategies = strategies;
        this.dataStore = dataStore;
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting financial data ingestion via Strategy Pattern...");

        for (FinancialDataStrategy strategy : strategies) {
            try {
                // Execute the strategy's fetch and transform logic
                Object transformedData = strategy.fetchAndTransform(restTemplate);

                // Store it using the resource type as the key
                dataStore.put(strategy.getResourceType(), transformedData);

                log.info("Successfully ingested resource: {}", strategy.getResourceType());
            } catch (Exception e) {
                // GRACEFUL ERROR HANDLING: Don't crash the app if one API call fails
                log.error("Failed to ingest {}: {}", strategy.getResourceType(), e.getMessage());
            }
        }

        // ATOMIC GREEN FIX: Seal the data store so it becomes immutable
        dataStore.markInitialized();
        log.info("Financial data store initialized and sealed.");
    }

}
