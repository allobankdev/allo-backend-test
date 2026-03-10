package com.allo.bank.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.allo.bank.dto.FinanceDataItem;
import com.allo.bank.service.store.InMemoryFinanceDataStore;
import com.allo.bank.strategy.IDRDataFetcher;

@Component
public class StartupDataLoader implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final InMemoryFinanceDataStore dataStore;

    public StartupDataLoader(List<IDRDataFetcher> fetchers, InMemoryFinanceDataStore dataStore) {
        this.fetchers = fetchers;
        this.dataStore = dataStore;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, List<FinanceDataItem>> loadedData = fetchers.stream()
            .collect(Collectors.toUnmodifiableMap(
                IDRDataFetcher::resourceType,
                fetcher -> List.copyOf(fetcher.fetch())
            ));

        dataStore.replaceAll(loadedData);
    }
}
