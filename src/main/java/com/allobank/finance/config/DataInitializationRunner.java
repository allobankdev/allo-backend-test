package com.allobank.finance.config;

import com.allobank.finance.repository.FinanceDataRepository;
import com.allobank.finance.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializationRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializationRunner.class);

    private final List<IDRDataFetcher> dataFetchers;
    private final FinanceDataRepository financeDataRepository;

    public DataInitializationRunner(List<IDRDataFetcher> dataFetchers, FinanceDataRepository financeDataRepository) {
        this.dataFetchers = dataFetchers;
        this.financeDataRepository = financeDataRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting data initialization...");
        for (IDRDataFetcher fetcher : dataFetchers) {
            try {
                fetcher.fetchAndCacheData();
            } catch (Exception e) {
                log.error("Failed to initialize data for fetcher: {}", fetcher.getClass().getSimpleName(), e);
            }
        }
        financeDataRepository.seal();
        log.info("Data initialization completed.");
    }
}
