package com.allobank.finance.runner;

import com.allobank.finance.service.FinanceDataStore;
import com.allobank.finance.service.strategy.IDRDataFetcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class FinanceDataLoaderRunner implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> fetcherMap;
    private final FinanceDataStore store;

    public FinanceDataLoaderRunner(
            List<IDRDataFetcher> fetchers,
            FinanceDataStore store) {
        this.fetcherMap = fetchers.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcher::getResourceType,
                        Function.identity()
                ));
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        fetcherMap.forEach((key, fetcher) ->
                store.put(key, fetcher.fetch()));
        store.freeze();
    }
}

