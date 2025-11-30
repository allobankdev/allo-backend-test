package com.test.frankfurterexchangerate.service.implement;

import com.test.frankfurterexchangerate.service.FinanceDataStore;
import com.test.frankfurterexchangerate.service.IDRDataFetcher;
import lombok.var;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class FinanceDataLoader implements ApplicationRunner {
    private final FinanceDataStore store;
    private final Map<String, IDRDataFetcher> strategies;

    public FinanceDataLoader(FinanceDataStore store, Map<String, IDRDataFetcher> strategies) {
        this.store = store;
        this.strategies = strategies;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (var entry : strategies.entrySet()) {
            var key = entry.getKey();
            var fetcher = entry.getValue();

            var data = fetcher.fetchData();
            store.save(key, data);
        }
    }
}
