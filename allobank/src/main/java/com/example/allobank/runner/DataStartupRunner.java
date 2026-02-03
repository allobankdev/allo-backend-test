package com.example.allobank.runner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.allobank.cache.ExchangeRateCache;
import com.example.allobank.strategy.IDRDataFetcher;

@Component
public class DataStartupRunner implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final ExchangeRateCache cache;

    public DataStartupRunner(
            List<IDRDataFetcher> fetchers,
            ExchangeRateCache cache
    ) {
        this.fetchers = fetchers;
        this.cache = cache;
    }

    @Override
    public void run(ApplicationArguments args) {

        Map<String, Object> result = new HashMap<>();

        for (IDRDataFetcher fetcher : fetchers) {
            result.put(
                    fetcher.getResourceType(),
                    fetcher.fetch()
            );
        }

        cache.load(result);
    }
}
