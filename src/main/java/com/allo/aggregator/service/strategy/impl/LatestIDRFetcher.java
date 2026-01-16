package com.allo.aggregator.service.strategy.impl;

import com.allo.aggregator.config.AppConfig;
import com.allo.aggregator.service.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;

@Component
public class LatestIDRFetcher implements IDRDataFetcher {

  private static final Logger log = LoggerFactory.getLogger(LatestIDRFetcher.class);
  private final WebClient webClient;
  private final AppConfig appConfig;

  @Autowired
  public LatestIDRFetcher(WebClient webClient, AppConfig appConfig) {
    this.webClient = webClient;
    this.appConfig = appConfig;
  }

  @Override
  public Map<String, Object> fetchData() {
    log.info("Fetching latest IDR rates...");
    try {
      Map<String, Object> result = webClient.get()
          .uri("/latest?base=IDR")
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
          })
          .block();

      if (result != null && result.containsKey("rates")) {
        @SuppressWarnings("unchecked")
        Map<String, Object> rates = (Map<String, Object>) result.get("rates");
        // Convert all Doubles to BigDecimal for readable formatting
        rates.replaceAll((k, v) -> {
          if (v instanceof Number) {
            return BigDecimal.valueOf(((Number) v).doubleValue());
          }
          return v;
        });

        if (rates.containsKey("USD")) {
          BigDecimal rateUsd = (BigDecimal) rates.get("USD");
          String username = (appConfig.getSpread() != null) ? appConfig.getSpread().getUsername() : null;
          Double spreadFactor = calculateSpreadFactor(username);

          // Formula: (1 / Rate_USD) * (1 + Spread Factor)
          // We calculate in double for precision then convert to BigDecimal
          double usdBuySpreadIdrVal = (1.0 / rateUsd.doubleValue()) * (1.0 + spreadFactor);

          result.put("USD_BuySpread_IDR", BigDecimal.valueOf(usdBuySpreadIdrVal));
          result.put("Spread_Factor_Used", spreadFactor);
        }
      }
      return result;
    } catch (Exception e) {
      log.error("Error fetching latest rates", e);
      throw new RuntimeException("Failed to fetch latest rates", e);
    }
  }

  private Double calculateSpreadFactor(String username) {
    if (username == null)
      return 0.0;
    int sum = 0;
    for (char c : username.toLowerCase().toCharArray()) {
      sum += c;
    }
    return (sum % 1000) / 100000.0;
  }

  @Override
  public String getResourceType() {
    return "latest_idr_rates";
  }
}
