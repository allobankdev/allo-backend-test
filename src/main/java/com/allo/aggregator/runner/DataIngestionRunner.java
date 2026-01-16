package com.allo.aggregator.runner;

import com.allo.aggregator.service.strategy.IDRDataFetcher;
import com.allo.aggregator.store.ExchangeRateStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataIngestionRunner implements ApplicationRunner {

  private static final Logger log = LoggerFactory.getLogger(DataIngestionRunner.class);
  private final List<IDRDataFetcher> fetchers;
  private final ExchangeRateStore store;

  public DataIngestionRunner(List<IDRDataFetcher> fetchers, ExchangeRateStore store) {
    this.fetchers = fetchers;
    this.store = store;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("Starting data ingestion from external API...");
    for (IDRDataFetcher fetcher : fetchers) {
      String resourceType = fetcher.getResourceType();
      log.info("Fetching data for resource: {}", resourceType);
      try {
        Object data = fetcher.fetchData();
        store.putData(resourceType, data);
        log.info("Successfully loaded data for: {}", resourceType);
      } catch (Exception e) {
        log.error("Failed to load data for resource: {}", resourceType, e);
      }
    }
    log.info("Data ingestion completed.");
  }
}
