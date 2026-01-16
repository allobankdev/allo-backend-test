package com.allo.aggregator.client;

import com.allo.aggregator.config.AppConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FrankfurterClientFactoryBean implements FactoryBean<WebClient> {

  private final AppConfig appConfig;

  public FrankfurterClientFactoryBean(AppConfig appConfig) {
    this.appConfig = appConfig;
  }

  @Override
  public WebClient getObject() throws Exception {
    return WebClient.builder()
        .baseUrl(appConfig.getBaseUrl())
        .build();
  }

  @Override
  public Class<?> getObjectType() {
    return WebClient.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
