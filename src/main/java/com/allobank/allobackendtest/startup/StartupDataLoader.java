package com.allobank.allobackendtest.startup;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.allobank.allobackendtest.model.DTO.LatestIdrRatesResponse;
import com.allobank.allobackendtest.service.LatestIdrRatesService;
import com.allobank.allobackendtest.store.InMemoryDataStore;
import com.allobank.allobackendtest.strategy.IDRDataFetcher;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StartupDataLoader implements CommandLineRunner {

    private final List<IDRDataFetcher> fetchers;
    private final InMemoryDataStore dataStore;
    private final LatestIdrRatesService latestIdrRatesService;

    public StartupDataLoader(List<IDRDataFetcher> fetchers, InMemoryDataStore dataStore,
            LatestIdrRatesService latestIdrRatesService) {
        this.fetchers = fetchers;
        this.dataStore = dataStore;
        this.latestIdrRatesService = latestIdrRatesService;
    }

    @Override
    public void run(String... args) {

        log.info("Starting IDR data preload process...");

        for (IDRDataFetcher fetcher : fetchers) {
            String resourceType = fetcher.getResourceType();

            try {
                log.info("Loading data for resourceType={}", resourceType);

                Object rawData = fetcher.fetchData();

                if (rawData == null) {
                    throw new IllegalStateException(
                            "Fetched data is null for resourceType=" + resourceType);
                }

                Object finalData = rawData;

                if ("latest_idr_rates".equals(resourceType)) {
                    finalData = latestIdrRatesService.applyUsdBuySpread(
                            (LatestIdrRatesResponse) rawData);
                }

                dataStore.put(resourceType, finalData);

                log.info("Successfully loaded data for resourceType={}", resourceType);

            } catch (Exception ex) {
                log.error("Failed to load data for resourceType={}", resourceType, ex);
            }
        }

        log.info("IDR data preload process completed");
    }

}
