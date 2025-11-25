package com.example.financedata.config;

import com.example.financedata.fetcher.HistoricalIdrUsdFetcher;
import com.example.financedata.fetcher.IDRDataFetcher;
import com.example.financedata.fetcher.LatestIdrRatesFetcher;
import com.example.financedata.fetcher.SupportedCurrenciesFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FetcherConfig {

    // TODO: change to your GitHub username if you want different spread factor
    private static final String GITHUB_USERNAME = "aswindew";

    @Bean
    public IDRDataFetcher latestIdrRatesFetcher(WebClient webClient) {
        return new LatestIdrRatesFetcher(webClient, GITHUB_USERNAME);
    }

    @Bean
    public IDRDataFetcher historicalIdrUsdFetcher(WebClient webClient) {
        return new HistoricalIdrUsdFetcher(webClient);
    }

    @Bean
    public IDRDataFetcher supportedCurrenciesFetcher(WebClient webClient) {
        return new SupportedCurrenciesFetcher(webClient);
    }
}
