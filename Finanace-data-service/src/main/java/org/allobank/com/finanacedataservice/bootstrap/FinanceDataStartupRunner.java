package org.allobank.com.finanacedataservice.bootstrap;

import org.allobank.com.finanacedataservice.clent.FrankfurterClient;
import org.allobank.com.finanacedataservice.service.FinanceDataCache;
import org.allobank.com.finanacedataservice.strategy.IdrDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FinanceDataStartupRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(FinanceDataStartupRunner.class);

    private final List<IdrDataFetcher> fetcherList;
    private final FrankfurterClient frankfurterClient;
    private final FinanceDataCache cache;

    public FinanceDataStartupRunner(List<IdrDataFetcher> fetcherList, FrankfurterClient frankfurterClient, FinanceDataCache cache) {
        this.fetcherList = fetcherList;
        this.frankfurterClient = frankfurterClient;
        this.cache = cache;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (IdrDataFetcher fetcher : fetcherList) {
            String resourceType = fetcher.getResourceType();
            try {
                fetcher.loadData(frankfurterClient, cache);
                log.info("load data succes for resource type {}", resourceType);
            } catch (Exception ex) {
                log.error("failed to load data for resource type = {}", resourceType);
            }
        }
    }
}
