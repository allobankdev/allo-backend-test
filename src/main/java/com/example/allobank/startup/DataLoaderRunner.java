package com.example.allobank.startup;

import com.example.allobank.store.FinanceDataStore;
import com.example.allobank.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataLoaderRunner implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> fetchers;
    private final FinanceDataStore store;

    @Override
    public void run(ApplicationArguments args) {
        fetchers.forEach((key, fetcher) -> {
            List<?> data = fetcher.fetchData();
            store.put(key, data);
        });
    }
}