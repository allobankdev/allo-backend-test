package com.allobank.financeaggregator.runner;

import com.allobank.financeaggregator.model.FinanceDataItem;
import com.allobank.financeaggregator.service.FinanceDataStore;
import com.allobank.financeaggregator.strategy.IDRDataFetcher;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class FinanceDataLoader implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> strategies;
    private final FinanceDataStore dataStore;

    public FinanceDataLoader(Map<String, IDRDataFetcher> strategies, FinanceDataStore dataStore) {
        this.strategies = strategies;
        this.dataStore = dataStore;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, List<FinanceDataItem<?>>> loaded = new HashMap<>();
        for (Map.Entry<String, IDRDataFetcher> entry : strategies.entrySet()) {
            String resourceType = entry.getKey();
            Object data = entry.getValue().fetchData();
            loaded.put(resourceType, List.of(new FinanceDataItem<>(resourceType, data)));
        }
        dataStore.load(loaded);
    }
}
