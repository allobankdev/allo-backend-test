package com.allobank.idr_rate_aggregator.config;

import com.allobank.idr_rate_aggregator.strategy.DataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataFetcherConfig implements ApplicationRunner {
    private final Map<String, DataFetcher> strategies;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        strategies.forEach((name, strategy) -> {
            strategy.refreshData();
        });
    }
}
