package com.finance.config;

import com.finance.service.IDRDataFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class AppConfig {
    @Bean
    public Map<String, IDRDataFetcher> fetcherMap(List<IDRDataFetcher> fetchers) {
        return fetchers.stream().collect(Collectors.toMap(IDRDataFetcher::resourceType, f->f));
    }
}

