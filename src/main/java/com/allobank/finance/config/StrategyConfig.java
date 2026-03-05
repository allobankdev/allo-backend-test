package com.allobank.finance.config;

import com.allobank.finance.strategy.IDRDataFetcher;
import com.allobank.finance.strategy.HistoricalIDRUSDStrategy;
import com.allobank.finance.strategy.LatestIDRRatesStrategy;
import com.allobank.finance.strategy.SupportedCurrenciesStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class StrategyConfig {

    @Bean
    public Map<String, IDRDataFetcher> strategyMap(
            LatestIDRRatesStrategy latestIDRRatesStrategy,
            HistoricalIDRUSDStrategy historicalIDRUSDStrategy,
            SupportedCurrenciesStrategy supportedCurrenciesStrategy) {

        return Map.of(
                latestIDRRatesStrategy.getResourceType(), latestIDRRatesStrategy,
                historicalIDRUSDStrategy.getResourceType(), historicalIDRUSDStrategy,
                supportedCurrenciesStrategy.getResourceType(), supportedCurrenciesStrategy
        );
    }
}
