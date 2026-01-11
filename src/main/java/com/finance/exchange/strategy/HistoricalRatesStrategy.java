package com.finance.exchange.strategy;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component("historical_idr_usd")
public class HistoricalRatesStrategy implements IDRDataFetcher {

  private final RestClient restClient;
  private final AtomicReference<Map<String, Object>> storage = new AtomicReference<>();

  public HistoricalRatesStrategy(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public String getResourceType() {
    return "historical_idr_usd";
  }

  @Override
  public void fetchData() {
    Map<String, Object> response = restClient.get()
        .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
        .retrieve()
        .body(new ParameterizedTypeReference<>() {
        });

    if (response != null) {
      storage.set(Collections.unmodifiableMap(response));
    }
  }

  @Override
  public Object getData() {
    return storage.get();
  }
}