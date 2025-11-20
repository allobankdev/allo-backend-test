package com.finance;

import com.finance.client.FrankfurterClient;
import com.finance.service.fetchers.HistoricalIdrUsdFetcher;
import com.finance.service.fetchers.LatestIdrRatesFetcher;
import com.finance.service.fetchers.SupportedCurrenciesFetcher;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @MockBean
    public FrankfurterClient client;

    @Bean
    public HistoricalIdrUsdFetcher historicalIdrUsdFetcher(FrankfurterClient client) {
        return new HistoricalIdrUsdFetcher(client);
    }

    @Bean
    public LatestIdrRatesFetcher latestIdrRatesFetcher(FrankfurterClient client) {
        String githubUserName = "xixixi";
        return new LatestIdrRatesFetcher(client, githubUserName);
    }

    @Bean
    public SupportedCurrenciesFetcher supportedCurrenciesFetcher(FrankfurterClient client) {
        return new SupportedCurrenciesFetcher(client);
    }

}
