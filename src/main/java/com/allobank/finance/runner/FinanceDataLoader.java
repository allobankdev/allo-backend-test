package com.allobank.finance.runner;

import com.allobank.finance.exception.ExternalApiException;
import com.allobank.finance.service.InMemoryFinanceStore;
import com.allobank.finance.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FinanceDataLoader implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceDataLoader.class);
    private final List<IDRDataFetcher> fetchers;
    private final InMemoryFinanceStore inMemoryFinanceStore;

    public FinanceDataLoader(List<IDRDataFetcher> fetchers, InMemoryFinanceStore financeStore) {
        this.fetchers = fetchers;
        this.inMemoryFinanceStore = financeStore;
    }

    @Override
    public void run(ApplicationArguments args) {

        Map<String, Object> dataMap = new HashMap<>();

        for (IDRDataFetcher fetcher : fetchers) {
            try {
                Object result = fetcher.fetch();
                dataMap.put(
                        fetcher.getResourceType(),
                        result
                );
                LOG.info("Loaded data for resourceType={}", fetcher.getResourceType());

            } catch (ExternalApiException ex) {

                LOG.error("Error loading data for resourceType={} | status={} | message= {}", fetcher.getResourceType(),
                        ex.getStatusCode(),
                        ex.getMessage()
                );
            } catch (Exception ex) {

                LOG.error("Unexpected error while loading data for resourceType= {}",
                        fetcher.getResourceType(),
                        ex
                );
            }

        }

        inMemoryFinanceStore.init(dataMap);

        LOG.info("Finance data loader finished for {} resources", dataMap.size());
    }
}
