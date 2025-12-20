package com.allo_backend_test.service;

import com.allo_backend_test.dto.FinanceResponseDto;
import com.allo_backend_test.strategy.IDRDataFetcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FinanceDataInitializer implements ApplicationRunner{

    private final List<IDRDataFetcher> fetchers;
    private final FinanceDataStore dataStore;

    public FinanceDataInitializer(List<IDRDataFetcher> fetchers,
                                  FinanceDataStore dataStore) {
        this.fetchers = fetchers;
        this.dataStore = dataStore;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            Map<String, List<FinanceResponseDto>> aggregatedData = new HashMap<>();

            for (IDRDataFetcher fetcher : fetchers) {
                aggregatedData.put(
                        fetcher.resourceType(),
                        fetcher.fetch()
                );
            }

            dataStore.initialize(aggregatedData);

        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Failed to initialize finance data from Frankfurter API",
                    ex
            );
        }
    }

}
