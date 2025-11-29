package com.allobank.backendtest.config;

import com.allobank.backendtest.fetcher.HistoricalIdrUsdFetcher;
import com.allobank.backendtest.fetcher.IDRDataFetcher;
import com.allobank.backendtest.fetcher.LatestIdrRatesFetcher;
import com.allobank.backendtest.fetcher.SupportedCurrenciesFetcher;
import com.allobank.backendtest.service.ImmutableFinanceStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(ExternalApiProperties.class)
public class AppConfig {
    @Bean
    public WebClientFactoryBean webClientFactoryBean(ExternalApiProperties props) {
        return new WebClientFactoryBean(props);
    }
    // Spring will expose WebClient produced by FactoryBean under the same bean name type

    @Bean
    public ImmutableFinanceStore immutableFinanceStore() {
        return new ImmutableFinanceStore();
    }

    // Register fetchers with explicit bean names that correspond to resource keys
    @Bean(name = "latest_idr_rates")
    public IDRDataFetcher latestFetcher(WebClient webClient, @Value("${app.github.username}") String gh) {
        return new LatestIdrRatesFetcher(webClient, gh);
    }

    @Bean(name = "historical_idr_usd")
    public IDRDataFetcher historicalFetcher(WebClient webClient) {
        return new HistoricalIdrUsdFetcher(webClient);
    }

    @Bean(name = "supported_currencies")
    public IDRDataFetcher supportedFetcher(WebClient webClient) {
        return new SupportedCurrenciesFetcher(webClient);
    }
}
