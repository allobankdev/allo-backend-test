package com.allobank.finance.scheduler;

import com.allobank.finance.service.InMemoryFinanceStore;
import com.allobank.finance.strategy.IDRDataFetcher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FinanceScheduler {
    private final List<IDRDataFetcher> fetchers;
    private final InMemoryFinanceStore inMemoryFinanceStore;

    public FinanceScheduler(List<IDRDataFetcher> fetchers,
                            InMemoryFinanceStore inMemoryFinanceStore) {
        this.fetchers = fetchers;
        this.inMemoryFinanceStore = inMemoryFinanceStore;
    }

    @Scheduled(fixedRateString = "${finance.refresh-interval-ms:300000}")
    public void refresh() {

        Map<String, Object> dataMap = new HashMap<>();

        for(IDRDataFetcher fetcher : fetchers) {
            dataMap.put(
                    fetcher.getResourceType(),
                    fetcher.fetch()
            );
        }

        inMemoryFinanceStore.init(dataMap);

        System.out.println("Finance data refreshed.");
    }
}
