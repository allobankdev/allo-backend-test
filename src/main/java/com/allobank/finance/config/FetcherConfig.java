package com.allobank.finance.config;

import com.allobank.finance.service.fetcher.IDRDataFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class FetcherConfig {

    @Bean
    public Map<String, IDRDataFetcher> fetcherMap(List<IDRDataFetcher> fetchers) {
        return fetchers.stream()
                .collect(Collectors.toMap(IDRDataFetcher::getResourceType, f -> f));
    }
}
