package com.finance.exchange.runner;

import com.finance.exchange.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FinanceDataInitializer implements ApplicationRunner {

  private static final Logger log = LoggerFactory.getLogger(FinanceDataInitializer.class);
  private final List<IDRDataFetcher> strategies;

  public FinanceDataInitializer(List<IDRDataFetcher> strategies) {
    this.strategies = strategies;
  }

  @Override
  public void run(ApplicationArguments args) {
    log.info("Starting Data Ingestion...");
    for (IDRDataFetcher strategy : strategies) {
      try {
        strategy.fetchData();
        log.info("Data loaded for: {}", strategy.getResourceType());
      } catch (Exception e) {
        log.error("Failed to load {}: {}", strategy.getResourceType(), e.getMessage());
      }
    }
    log.info("Ingestion Complete.");
  }
}