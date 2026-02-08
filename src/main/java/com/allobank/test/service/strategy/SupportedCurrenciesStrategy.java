package com.allobank.test.service.strategy;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

  private final RestTemplate restTemplate;

  public SupportedCurrenciesStrategy(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public String getResourceType() {
    return "supported_currencies";
  }

  @Override
  public Object fetchData() {
    return restTemplate.getForObject("/currencies", Map.class);
  }
}