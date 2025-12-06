package com.bank.allo.config;

import com.bank.allo.client.fetcher.HistoricalIdrUsdFetcher;
import com.bank.allo.client.fetcher.LatestIdrRatesFetcher;
import com.bank.allo.client.fetcher.SupportedCurrenciesFetcher;
import com.bank.allo.properties.AppProperties;
import com.bank.allo.repository.outbound.FrankfurterClientRepository;
import com.bank.allo.usecase.idr.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
public class StrategyConfig {

    @Bean
    public LatestIdrRatesFetcher latestFetcher(
            FrankfurterClientRepository repo,
            AppProperties props) {
        return new LatestIdrRatesFetcher(repo, props.getGithubUsername());
    }

    @Bean
    public HistoricalIdrUsdFetcher historicalFetcher(
            FrankfurterClientRepository repo) {
        return new HistoricalIdrUsdFetcher(repo);
    }

    @Bean
    public SupportedCurrenciesFetcher supportedFetcher(
            FrankfurterClientRepository repo) {
        return new SupportedCurrenciesFetcher(repo);
    }

    @Bean(name = "idrFetcherRegistry")
    public Map<String, IdrDataFetcher> idrFetcherRegistry(
            LatestIdrRatesFetcher f1,
            HistoricalIdrUsdFetcher f2,
            SupportedCurrenciesFetcher f3) {
        return Map.of(
                f1.resourceType(), f1,
                f2.resourceType(), f2,
                f3.resourceType(), f3);
    }

}
