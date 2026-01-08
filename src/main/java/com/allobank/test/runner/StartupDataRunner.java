package com.allobank.test.runner;

import com.allobank.test.service.DataCacheService;
import com.allobank.test.service.strategy.IDRDataFetcher;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dibuat oleh: Andre Rizaldi Brillianto
 * Email: andrerizaldib@gmail.com
 * Date: Wednesday, 7-January-2026
 * description: allo-bank-test
 */
@Component
public class StartupDataRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(StartupDataRunner.class);

    // spring mencari semua bean yang mengimplementasikan
    // interface IDRDataFetcher
    private final List<IDRDataFetcher> fetchers;
    // data cache service
    private final DataCacheService dataCacheService;

    public StartupDataRunner(List<IDRDataFetcher> fetchers, DataCacheService dataCacheService) {
        this.fetchers = fetchers;
        this.dataCacheService = dataCacheService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Starting data fetching for {} strategies", fetchers.size());
        Map<String, Object> aggregatedData = new HashMap<>();

        for (IDRDataFetcher fetcher : fetchers) {
            String resourceType = fetcher.getResourceType();
            try {
                logger.info("Fetching data for resource: {}", resourceType);
                Object data = fetcher.fetchData();
                aggregatedData.put(resourceType, data);
            } catch (Exception e) {
                logger.error("Failed to fetch data for resource: {}", resourceType, e);
                throw new RuntimeException("Critical failure fetching data for " + resourceType, e);
            }
        }

        dataCacheService.initializeData(aggregatedData);
        logger.info("Data fetching completed and cached successfully.");
    }
}
