package com.allo.test.modules.finance.loader;

import com.allo.test.modules.finance.client.IDRDataFetcher;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.service.DataStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Application startup loader that fetches all IDR exchange rate data
 * from the Frankfurter API exactly once and stores it in memory.
 * <p>
 * Uses a strategy pattern with automatic injection of all IDRDataFetcher implementations.
 * This ensures data is available immediately without needing to call
 * the external API on every request.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StartupLoader implements ApplicationRunner {

    private final List<IDRDataFetcher> idrDataFetchers;
    private final DataStoreService dataStoreService;
    private final WebClient webClient;

    @Override
    public void run(ApplicationArguments args) {
        log.info("=== Starting IDR exchange rate data loading at application startup ===");

        for (IDRDataFetcher strategy : idrDataFetchers) {
            ResourceType resourceType = strategy.getResourceType();
            log.info("Loading {}...", resourceType);

            try {
                strategy.fetchData(webClient);
            } catch (Exception e) {
                log.error("Error loading {}: {}", resourceType, e.getMessage());
            }
        }

        // Verify all data is loaded
        if (dataStoreService.isDataLoaded()) {
            log.info("=== IDR exchange rate data loading completed successfully ===");
            log.info("All three resources (latest_idr_rates, historical_idr_usd, supported_currencies) are available");
        } else {
            log.error("=== Data loading incomplete - some resources failed to load ===");
        }
    }
}
