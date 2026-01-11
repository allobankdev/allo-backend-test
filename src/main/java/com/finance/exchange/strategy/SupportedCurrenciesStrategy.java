package com.finance.exchange.strategy;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component("supported_currencies")
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

  private final RestClient restClient;
  private final AtomicReference<Map<String, String>> storage = new AtomicReference<>();

  public SupportedCurrenciesStrategy(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public String getResourceType() {
    return "supported_currencies";
  }

  @Override
  public void fetchData() {
    Map<String, String> response = restClient.get()
        .uri("/currencies")
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