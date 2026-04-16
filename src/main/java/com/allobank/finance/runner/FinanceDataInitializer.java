package com.allobank.finance.runner;

import com.allobank.finance.exception.FinanceDataLoadException;
import com.allobank.finance.store.FinanceDataStore;
import com.allobank.finance.strategy.IDRDataFetcher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class FinanceDataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(FinanceDataInitializer.class);

    private final Map<String, IDRDataFetcher> fetcherMap;
    private final FinanceDataStore dataStore;

    public FinanceDataInitializer(
            @Qualifier("idrDataFetcherMap") Map<String, IDRDataFetcher> fetcherMap,
            FinanceDataStore dataStore) {
        this.fetcherMap = fetcherMap;
        this.dataStore = dataStore;
    }

    @Bean
    public ApplicationRunner dataInitializerRunner() {
        return args -> {
            logger.info("Starting finance data initialization");
            Map<String, List<Map<String, Object>>> aggregated = new LinkedHashMap<>();

            fetcherMap.forEach((key, fetcher) -> {
                try {
                    List<Map<String, Object>> data = fetcher.fetchData();
                    aggregated.put(key, data);
                    logger.info("Loaded finance data for resource {}", key);
                } catch (Exception e) {
                    throw new FinanceDataLoadException("Startup finance data load failed for resource: " + key, e);
                }
            });

            dataStore.replaceAll(aggregated);
            logger.info("Finance data initialization completed");
        };
    }
}
