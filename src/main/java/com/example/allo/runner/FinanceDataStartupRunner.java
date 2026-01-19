package com.example.allo.runner;

import com.example.allo.service.FinanceDataStore;
import com.example.allo.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FinanceDataStartupRunner implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final FinanceDataStore store;

    public FinanceDataStartupRunner(
            List<IDRDataFetcher> fetchers,
            FinanceDataStore store) {
        this.fetchers = fetchers;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, Object> result = new HashMap<>();

        for (IDRDataFetcher f : fetchers) {
            result.put(f.getResourceType(), f.fetch());
        }

        store.init(result);
    }
}

