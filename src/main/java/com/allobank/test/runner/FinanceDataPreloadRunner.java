package com.allobank.test.runner;

import com.allobank.test.store.FinanceDataStore;
import com.allobank.test.strategy.IDRDataFetcher;
import com.allobank.test.strategy.IDRDataFetcherRegistry;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "finance.preload.enabled", havingValue = "true", matchIfMissing = true)
public class FinanceDataPreloadRunner implements ApplicationRunner {

    private final IDRDataFetcherRegistry registry;
    private final FinanceDataStore financeDataStore;

    public FinanceDataPreloadRunner(IDRDataFetcherRegistry registry, FinanceDataStore financeDataStore) {
        this.registry = registry;
        this.financeDataStore = financeDataStore;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, Object> loadedData = new LinkedHashMap<>();
        for (Map.Entry<String, IDRDataFetcher> entry : registry.asMap().entrySet()) {
            loadedData.put(entry.getKey(), entry.getValue().fetch());
        }
        financeDataStore.initializeOnce(loadedData);
    }
}
