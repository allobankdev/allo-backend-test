package com.allobank.finance.idrrateaggregator.service;

import com.allobank.finance.idrrateaggregator.startegy.IDRDataFetcher;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class FinanceDataInitializer {

    @Bean
    ApplicationRunner loadData(Map<String, IDRDataFetcher> strategies, FinanceDataStore store) {
        return args -> {
            var loadedData = strategies.values()
                    .stream()
                    .collect(Collectors.toMap(
                            IDRDataFetcher::getResourceType,
                            IDRDataFetcher::fetch
                    ));

            store.initialize(loadedData);
        };
    }
}
