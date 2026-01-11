package com.finance.exchange.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class FinanceClientFactoryBean implements FactoryBean<RestClient> {

  @Value("${frankfurter.api.url}")
  private String baseUrl;

  @Override
  public RestClient getObject() {
    return RestClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader("User-Agent", "IDR-Rate-Aggregator/1.0")
        .build();
  }

  @Override
  public Class<?> getObjectType() {
    return RestClient.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}