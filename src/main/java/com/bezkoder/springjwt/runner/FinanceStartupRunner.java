package com.bezkoder.springjwt.runner;

import com.bezkoder.springjwt.service.StrategyRegistry;
import com.bezkoder.springjwt.store.FinanceDataStore;
import com.bezkoder.springjwt.strategy.IDRDataFetcherStrategy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class FinanceStartupRunner implements ApplicationRunner {

    private final StrategyRegistry registry;
    private final FinanceDataStore store;

    public FinanceStartupRunner(StrategyRegistry registry, FinanceDataStore store) {
        this.registry = registry;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, List<Object>> loaded = new HashMap<>();

        for (IDRDataFetcherStrategy s : registry.strategyMap().values()) {
            s.loadAtStartup();
            loaded.put(s.resourceType(), s.loadedData());
        }

        store.initialize(loaded);
    }
}
