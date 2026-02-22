package com.allobank.finance.scheduler;

import com.allobank.finance.exception.ExternalApiException;
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

    @Scheduled(fixedRateString = "${finance.refresh-interval-ms:300000}",
            initialDelayString = "${finance.refresh-initial-delay-ms:300000}")
    public void refresh() {

        LOG.info("Refreshing finance data");

        Map<String, Object> dataMap = new HashMap<>();

        for (IDRDataFetcher fetcher : fetchers) {

            try {
                Object result = fetcher.fetch();
                dataMap.put(fetcher.getResourceType(), result);

                LOG.info("Refreshed resourceType{}", fetcher.getResourceType());

            } catch (ExternalApiException ex) {

                LOG.warn("Failed to refresh data for resourceType{} | status={}", fetcher.getResourceType(), ex.getStatusCode());

            } catch (Exception ex) {

                LOG.error("Unexpected error while refreshing data for resourceType{}", fetcher.getResourceType(), ex);
            }
        }

        if (!dataMap.isEmpty()) {
            inMemoryFinanceStore.init(dataMap);
            LOG.info("Successfully refreshed data for {} resources", dataMap.size());
        } else {
            LOG.warn("Finance data refreshed but no resources were updated");
        }


    }
}
