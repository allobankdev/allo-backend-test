package com.allobank.idr.config;

import com.allobank.idr.strategy.IDRDataFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class StrategyConfig {
    
    @Bean
    public Map<String, IDRDataFetcher> dataFetcherMap(List<IDRDataFetcher> fetchers) {
        return fetchers.stream()
                .collect(Collectors.toMap(
                    fetcher -> fetcher.getResourceType(),
                    fetcher -> fetcher
                ));
    }
}
