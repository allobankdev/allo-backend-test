package com.allobank.frankfurter.config;

import com.allobank.frankfurter.service.strategy.IDRDataFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class StrategyConfig {

    @Bean
    public Map<String, IDRDataFetcher> fetcherMap(List<IDRDataFetcher> fetchers) {
        return fetchers.stream()
                .collect(Collectors.toMap(IDRDataFetcher::getResourceType, Function.identity()));
    }
}