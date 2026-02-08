package com.allobank.test.service;

import com.allobank.test.service.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateStore implements ApplicationRunner {

  private final List<IDRDataFetcher> fetchers;
  private final Map<String, Object> dataStore = new ConcurrentHashMap<>();

  @Override
  public void run(ApplicationArguments args) {
    log.info("Fetching initial data...");
    fetchers.forEach(fetcher -> {
      try {
        dataStore.put(fetcher.getResourceType(), fetcher.fetchData());
        log.info("Loaded data for: {}", fetcher.getResourceType());
      } catch (Exception e) {
        log.error("Failed to load: {}", fetcher.getResourceType(), e);
      }
    });
  }

  public Object getData(String resourceType) {
    return dataStore.get(resourceType);
  }
}