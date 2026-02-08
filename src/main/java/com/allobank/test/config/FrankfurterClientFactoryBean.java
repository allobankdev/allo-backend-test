package com.allobank.test.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Component
public class FrankfurterClientFactoryBean implements FactoryBean<RestTemplate>, InitializingBean {

  private RestTemplate restTemplate;

  @Value("${frankfurter.api.base-url}")
  private String baseUrl;

  @Override
  public RestTemplate getObject() {
    return restTemplate;
  }

  @Override
  public Class<?> getObjectType() {
    return RestTemplate.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public void afterPropertiesSet() {
    this.restTemplate = new RestTemplateBuilder()
        .rootUri(baseUrl)
        .setConnectTimeout(Duration.ofSeconds(10))
        .setReadTimeout(Duration.ofSeconds(10))
        .build();
  }
}