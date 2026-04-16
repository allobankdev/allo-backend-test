package com.allobank.finance.config;

import com.allobank.finance.strategy.HistoricalIdrUsdFetcher;
import com.allobank.finance.strategy.IDRDataFetcher;
import com.allobank.finance.strategy.LatestIdrRatesFetcher;
import com.allobank.finance.strategy.SupportedCurrenciesFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class FetcherStrategyConfig {

    @Bean
    public Map<String, IDRDataFetcher> idrDataFetcherMap(
            LatestIdrRatesFetcher latestFetcher,
            HistoricalIdrUsdFetcher historicalFetcher,
            SupportedCurrenciesFetcher currenciesFetcher) {
        Map<String, IDRDataFetcher> strategies = new LinkedHashMap<>();
        strategies.put("latest_idr_rates", latestFetcher);
        strategies.put("historical_idr_usd", historicalFetcher);
        strategies.put("supported_currencies", currenciesFetcher);
        return Map.copyOf(strategies);
    }
}
