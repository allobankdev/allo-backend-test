package com.allobank.test.service.strategy;

import com.allobank.test.dto.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class LatestIDRRatesStrategy implements IDRDataFetcher {

  private final RestTemplate restTemplate;
  private final String githubUsername;

  public LatestIDRRatesStrategy(RestTemplate restTemplate,
      @Value("${app.github.username}") String githubUsername) {
    this.restTemplate = restTemplate;
    this.githubUsername = githubUsername;
  }

  @Override
  public String getResourceType() {
    return "latest_idr_rates";
  }

  @Override
  public Object fetchData() {
    ExchangeRateResponse response = restTemplate.getForObject("/latest?base=IDR", ExchangeRateResponse.class);

    if (response != null && response.getRates() != null && response.getRates().containsKey("USD")) {
      double rateUsd = response.getRates().get("USD");
      double spreadFactor = calculateSpreadFactor(githubUsername);

      double usdBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);

      Map<String, Object> result = new HashMap<>();
      result.put("base", response.getBase());
      result.put("date", response.getDate());
      result.put("rates", response.getRates());
      result.put("USD_BuySpread_IDR", usdBuySpreadIdr);
      return result;
    }
    return response;
  }

  private double calculateSpreadFactor(String username) {
    if (username == null || username.isEmpty())
      return 0.0;
    int sum = username.chars().sum();
    return (sum % 1000) / 100000.0;
  }
}