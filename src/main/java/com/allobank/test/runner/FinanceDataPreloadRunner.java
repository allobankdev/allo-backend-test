package com.allobank.test.runner;

import com.allobank.test.store.FinanceDataStore;
import com.allobank.test.strategy.IDRDataFetcher;
import com.allobank.test.strategy.IDRDataFetcherRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "finance.preload.enabled", havingValue = "true", matchIfMissing = true)
public class FinanceDataPreloadRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(FinanceDataPreloadRunner.class);

    private final IDRDataFetcherRegistry registry;
    private final FinanceDataStore financeDataStore;
    private final boolean failFast;

    public FinanceDataPreloadRunner(
            IDRDataFetcherRegistry registry,
            FinanceDataStore financeDataStore,
            @Value("${finance.preload.fail-fast:false}") boolean failFast
    ) {
        this.registry = registry;
        this.financeDataStore = financeDataStore;
        this.failFast = failFast;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, Object> loadedData = new LinkedHashMap<>();
        for (Map.Entry<String, IDRDataFetcher> entry : registry.asMap().entrySet()) {
            String resourceType = entry.getKey();
            try {
                loadedData.put(resourceType, entry.getValue().fetch());
            } catch (Exception exception) {
                if (failFast) {
                    throw exception;
                }
                String message = exception.getMessage() == null ? "Unknown upstream error" : exception.getMessage();
                log.warn("Preload failed for resourceType='{}'. App will continue with fallback payload. Cause: {}", resourceType, message);
                loadedData.put(resourceType, java.util.List.of(Map.of(
                        "resourceType", resourceType,
                        "status", "unavailable",
                        "message", "Upstream source unavailable during preload",
                        "details", message,
                        "at", OffsetDateTime.now().toString()
                )));
            }
        }
        financeDataStore.initializeOnce(loadedData);
    }
}
