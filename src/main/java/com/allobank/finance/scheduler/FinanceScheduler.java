package com.allobank.finance.scheduler;

import com.allobank.finance.service.InMemoryFinanceStore;
import com.allobank.finance.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FinanceScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceScheduler.class);
    private final List<IDRDataFetcher> fetchers;
    private final InMemoryFinanceStore inMemoryFinanceStore;

    public FinanceScheduler(List<IDRDataFetcher> fetchers,
                            InMemoryFinanceStore inMemoryFinanceStore) {
        this.fetchers = fetchers;
        this.inMemoryFinanceStore = inMemoryFinanceStore;
    }

    @Scheduled(fixedRateString = "${finance.refresh-interval-ms:300000}")
    public void refresh() {

        LOG.info("Refreshing finance data");

        try {
            Map<String, Object> dataMap = new HashMap<>();

            for(IDRDataFetcher fetcher : fetchers) {
                dataMap.put(
                        fetcher.getResourceType(),
                        fetcher.fetch()
                );
            }

            inMemoryFinanceStore.init(dataMap);

            LOG.info("Finance data refreshed");
        } catch (Exception ex) {
            LOG.error("Error occurred while refreshing finance data!", ex);
        }


    }
}
