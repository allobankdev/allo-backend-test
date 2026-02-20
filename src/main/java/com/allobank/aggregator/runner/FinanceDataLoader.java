package com.allobank.aggregator.runner;

import com.allobank.aggregator.dto.FinanceDataDto;
import com.allobank.aggregator.service.FinanceDataStore;
import com.allobank.aggregator.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FinanceDataLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(FinanceDataLoader.class);

    private final List<IDRDataFetcher> fetchers;
    private final FinanceDataStore store;

    public FinanceDataLoader(List<IDRDataFetcher> fetchers, FinanceDataStore store) {
        this.fetchers = fetchers;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("FinanceDataLoader starting - fetching all resources once");
        Map<String, FinanceDataDto> temp = new HashMap<>();
        for (IDRDataFetcher fetcher : fetchers) {
            try {
                FinanceDataDto dto = fetcher.fetch();
                temp.put(fetcher.resourceKey(), dto);
                log.info("Loaded resource: {}", fetcher.resourceKey());
            } catch (Exception e) {
                log.error("Failed to load resource {}: {}", fetcher.resourceKey(), e.getMessage(), e);
                throw new RuntimeException("Initialization failed for " + fetcher.resourceKey(), e);
            }
        }
        store.initialize(temp);
        log.info("FinanceDataLoader finished initialization");
    }
}
