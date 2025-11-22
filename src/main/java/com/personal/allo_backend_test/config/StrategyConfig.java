package com.personal.allo_backend_test.config;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.personal.allo_backend_test.strategy.IDRDataFetcher;

@Configuration
public class StrategyConfig {
  @Bean
  public Map<String, IDRDataFetcher> idrDataFetcherStrategies(List<IDRDataFetcher> strategies) {
    return strategies.stream()
      .collect(Collectors.toMap(
        IDRDataFetcher::getResourceType,
        Function.identity()
      ));
  }
}

