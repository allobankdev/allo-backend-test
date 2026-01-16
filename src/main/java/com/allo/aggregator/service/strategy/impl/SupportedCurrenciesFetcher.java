package com.allo.aggregator.service.strategy.impl;

import com.allo.aggregator.service.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

  private static final Logger log = LoggerFactory.getLogger(SupportedCurrenciesFetcher.class);
  private final WebClient webClient;

  public SupportedCurrenciesFetcher(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public Object fetchData() {
    log.info("Fetching supported currencies...");
    try {
      return webClient.get()
          .uri("/currencies")
          .retrieve()
          .bodyToMono(Map.class)
          .block();
    } catch (Exception e) {
      log.error("Error fetching currencies", e);
      throw new RuntimeException("Failed to fetch currencies", e);
    }
  }

  @Override
  public String getResourceType() {
    return "supported_currencies";
  }
}
