package com.personal.allo_backend_test.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "client.frankfurter")
public class FrankfurterClientProperties {
  private String baseUrl;
  private Duration connectTimeout;
  private Duration readTimeout;
  private Duration writeTimeout;
  private int maxInMemorySizeInMb;
  private RateProperties rate;

  @Getter
  @Setter
  public static class RateProperties {
    private String from;
    private String to;
    private String historicalStartDate;
    private String historicalEndDate;
  }
}

