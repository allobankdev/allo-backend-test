package com.allo.aggregator.service.strategy.impl;

import org.springframework.core.ParameterizedTypeReference;

import com.allo.aggregator.service.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class HistoricalIDRFetcher implements IDRDataFetcher {

  private static final Logger log = LoggerFactory.getLogger(HistoricalIDRFetcher.class);
  private final WebClient webClient;

  public HistoricalIDRFetcher(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public Object fetchData() {
    log.info("Fetching historical IDR-USD data...");
    try {
      Map<String, Object> result = webClient.get()
          .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
          })
          .block();

      if (result != null && result.containsKey("rates")) {
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> rates = (Map<String, Map<String, Object>>) result.get("rates");
        rates.forEach((date, dateRates) -> {
          dateRates.replaceAll((k, v) -> {
            if (v instanceof Number) {
              return BigDecimal.valueOf(((Number) v).doubleValue());
            }
            return v;
          });
        });
      }
      return result;
    } catch (Exception e) {
      log.error("Error fetching historical data", e);
      throw new RuntimeException("Failed to fetch historical data", e);
    }
  }

  @Override
  public String getResourceType() {
    return "historical_idr_usd";
  }
}
