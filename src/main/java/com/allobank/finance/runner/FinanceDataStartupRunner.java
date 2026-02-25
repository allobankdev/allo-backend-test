package com.allobank.finance.runner;

import com.allobank.finance.dto.FinanceDataResponse;
import com.allobank.finance.entity.FinanceDataCache;
import com.allobank.finance.fetcher.IDRDataFetcher;
import com.allobank.finance.repository.FinanceDataCacheRepository;
import com.allobank.finance.service.FinanceDataStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class FinanceDataStartupRunner implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> fetcherMap;
    private final FinanceDataStore financeDataStore;
    private final FinanceDataCacheRepository cacheRepository;
    private final ObjectMapper objectMapper;

    public FinanceDataStartupRunner(
            @Qualifier("fetcherMap") Map<String, IDRDataFetcher> fetcherMap,
            FinanceDataStore financeDataStore,
            FinanceDataCacheRepository cacheRepository,
            ObjectMapper objectMapper) {
        this.fetcherMap = fetcherMap;
        this.financeDataStore = financeDataStore;
        this.cacheRepository = cacheRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("=== Starting financial data initialization ===");
        log.info("Fetching {} resource(s): {}", fetcherMap.size(), fetcherMap.keySet());

        int successCount = 0;
        int failCount = 0;

        for (Map.Entry<String, IDRDataFetcher> entry : fetcherMap.entrySet()) {
            String resourceType = entry.getKey();
            IDRDataFetcher fetcher = entry.getValue();

            try {
                log.info("Fetching resource: {}", resourceType);
                FinanceDataResponse response = fetcher.fetch();

                financeDataStore.put(resourceType, response);

                String jsonPayload = objectMapper.writeValueAsString(response);
                FinanceDataCache cacheEntry = FinanceDataCache.builder()
                        .resourceType(resourceType)
                        .jsonPayload(jsonPayload)
                        .build();
                cacheRepository.save(cacheEntry);

                log.info("Successfully loaded and persisted resource: {}", resourceType);
                successCount++;

            } catch (Exception e) {
                log.error("Failed to fetch resource '{}': {}", resourceType, e.getMessage(), e);
                failCount++;
            }
        }

        financeDataStore.seal();

        log.info("=== Financial data initialization complete: {}/{} resources loaded successfully ===",
                successCount, fetcherMap.size());

        if (failCount > 0) {
            log.warn("{} resource(s) failed to load.", failCount);
        }
    }
}