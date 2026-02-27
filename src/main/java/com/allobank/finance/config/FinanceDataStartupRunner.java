package com.allobank.finance.config;

import com.allobank.finance.model.response.FinanceDataResponse;
import com.allobank.finance.service.FinanceDataStore;
import com.allobank.finance.service.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FinanceDataStartupRunner implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> fetchers;
    private final FinanceDataStore store;

    @Override
    public void run(ApplicationArguments args) {

        Map<String, FinanceDataResponse> loaded = new HashMap<>();

        fetchers.forEach((key, fetcher) -> {
            Object data = fetcher.fetchData();

            loaded.put(key, new FinanceDataResponse(
                    key,
                    ZonedDateTime.now(),
                    data
            ));
        });

        store.load(loaded);
    }
}
