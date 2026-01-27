package com.interview.backend.runner;

import com.interview.backend.services.FinanceDataStore;
import com.interview.backend.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceDataLoader implements ApplicationRunner {

    private final List<IDRDataFetcher> dataFetchers;
    private final FinanceDataStore dataStore;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        try {
            // Fetch data from all strategies
            for (IDRDataFetcher fetcher : dataFetchers) {
                String resourceType = fetcher.getResourceType();

                Map<String, Object> data = fetcher.fetchData();
                dataStore.storeData(resourceType, data);

            }

            dataStore.markAsInitialized();

        } catch (Exception e) {
            throw new RuntimeException("Application startup failed due to data initialization error", e);
        }
    }
}
