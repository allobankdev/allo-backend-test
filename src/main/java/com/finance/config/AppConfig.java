package com.finance.config;

import com.finance.service.DataFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class AppConfig {
    @Bean
    public Map<String, DataFetcher> fetcherMap(List<DataFetcher> fetchers) {
        return fetchers.stream().collect(Collectors.toMap(DataFetcher::resourceType, f->f));
    }
}

