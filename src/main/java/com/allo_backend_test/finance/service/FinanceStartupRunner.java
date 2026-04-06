
package com.allo_backend_test.finance.service;

import com.allo_backend_test.finance.adapter.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FinanceStartupRunner implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final FinanceDataStore store;

    @Override
    public void run(ApplicationArguments args) {
        Map<String, Object> aggregated = new HashMap<>();
        fetchers.forEach(fetcher ->
                aggregated.put(fetcher.getResourceType(),
                        fetcher.fetchAndTransform())
        );
        store.loadData(aggregated);
    }
}
