package com.allo.aggregator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aggregator.api")
public class AppConfig {
  private String baseUrl;
  private Spread spread;

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public Spread getSpread() {
    return spread;
  }

  public void setSpread(Spread spread) {
    this.spread = spread;
  }

  public static class Spread {
    private String username;

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }
  }
}
