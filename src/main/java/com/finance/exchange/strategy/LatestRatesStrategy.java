package com.finance.exchange.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component("latest_idr_rates")
public class LatestRatesStrategy implements IDRDataFetcher {

  private final RestClient restClient;
  private final AtomicReference<Map<String, Object>> storage = new AtomicReference<>();

  @Value("${github.username}")
  private String githubUsername;

  public LatestRatesStrategy(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public String getResourceType() {
    return "latest_idr_rates";
  }

  @Override
  public void fetchData() {
    Map<String, Object> response = restClient.get()
        .uri("/latest?base=IDR")
        .retrieve()
        .body(new ParameterizedTypeReference<>() {
        });

    if (response != null && response.containsKey("rates")) {
      double spreadFactor = calculateSpreadFactor(githubUsername);

      @SuppressWarnings("unchecked")
      Map<String, Double> rates = (Map<String, Double>) response.get("rates");

      if (rates != null && rates.containsKey("USD")) {
        double rateUsd = rates.get("USD");
        double usdBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);

        response.put("USD_BuySpread_IDR", usdBuySpreadIdr);
        response.put("meta_spread_factor_used", spreadFactor);
      }

      storage.set(Collections.unmodifiableMap(response));
    }
  }

  @Override
  public Object getData() {
    return storage.get();
  }

  private double calculateSpreadFactor(String username) {
    if (username == null || username.isBlank())
      return 0.0;
    long sum = username.toLowerCase().chars().sum();
    return (sum % 1000) / 100000.0;
  }
}