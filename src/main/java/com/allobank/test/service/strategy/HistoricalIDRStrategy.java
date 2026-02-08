package com.allobank.test.service.strategy;

import com.allobank.test.dto.ExchangeRateResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HistoricalIDRStrategy implements IDRDataFetcher {

  private final RestTemplate restTemplate;

  public HistoricalIDRStrategy(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public String getResourceType() {
    return "historical_idr_usd";
  }

  @Override
  public Object fetchData() {
    return restTemplate.getForObject("/2024-01-01..2024-01-05?from=IDR&to=USD", ExchangeRateResponse.class);
  }
}