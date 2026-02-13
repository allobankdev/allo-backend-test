package com.allo.test.runner;

import com.allo.test.strategy.FinanceStrategyFactory;
import com.allo.test.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FrankfurterRunner implements ApplicationRunner {

    private final FinanceStrategyFactory financeStrategyFactory;

    public FrankfurterRunner(FinanceStrategyFactory financeStrategyFactory) {
        this.financeStrategyFactory = financeStrategyFactory;
    }

    @Override
    public void run(ApplicationArguments args) {

        log.info("==STARTING APPLICATION==");
        fetchAndCache("latest_idr_rates");
        fetchAndCache("historical_idr_usd");
        fetchAndCache("currencies");

        log.info("Data cached successfully");
        log.info("==APPLICATION STARTED==");
    }

    private void fetchAndCache(String key) {
        IDRDataFetcher fetcher = financeStrategyFactory.getStrategy(key);
        fetcher.fetchData();
    }
}
