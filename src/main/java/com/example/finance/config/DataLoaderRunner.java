package com.example.finance.config;

import com.example.finance.service.FinanceDataService;
import com.example.finance.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DataLoaderRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoaderRunner.class);

    private final Map<String, IDRDataFetcher> fetchers;
    private final FinanceDataService dataService;

    public DataLoaderRunner(Map<String, IDRDataFetcher> fetchers, FinanceDataService dataService) {
        this.fetchers = fetchers;
        this.dataService = dataService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting data loader");
        for (Map.Entry<String, IDRDataFetcher> entry : fetchers.entrySet()) {
            String key = entry.getKey();
            IDRDataFetcher fetcher = entry.getValue();
            try {
                List<Map<String, Object>> data = fetcher.fetchData();
                dataService.setData(key, data);
                log.info("Loaded resource {} with {} items", key, data.size());
            } catch (Exception e) {
                log.error("Failed to load data for {}", key, e);
                // could rethrow or continue depending on desired behavior
                throw e;
            }
        }
        dataService.markInitialized();
        log.info("Data loader finished");
    }
}