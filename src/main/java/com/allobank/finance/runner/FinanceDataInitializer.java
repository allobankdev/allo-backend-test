package com.allobank.finance.runner;

import com.allobank.finance.service.InMemoryFinanceStore;
import com.allobank.finance.strategy.IDRDataFetcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FinanceDataInitializer implements ApplicationRunner {
    private final List<IDRDataFetcher> fetchers;
    private final InMemoryFinanceStore inMemoryFinanceStore;

    public FinanceDataInitializer(List<IDRDataFetcher> fetchers, InMemoryFinanceStore financeStore) {
        this.fetchers = fetchers;
        this.inMemoryFinanceStore = financeStore;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> loaded = fetchers.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcher::getResourceType,
                        IDRDataFetcher::fetch
                ));
        inMemoryFinanceStore.init(loaded);
    }
}
