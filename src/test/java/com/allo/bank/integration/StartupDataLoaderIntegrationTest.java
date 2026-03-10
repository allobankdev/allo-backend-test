package com.allo.bank.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.allo.bank.client.FrankfurterClient;
import com.allo.bank.client.dto.FrankfurterHistoricalResponse;
import com.allo.bank.client.dto.FrankfurterLatestResponse;
import com.allo.bank.config.AppProperties;
import com.allo.bank.service.DefaultFinanceDataService;
import com.allo.bank.service.FinanceDataService;
import com.allo.bank.service.StartupDataLoader;
import com.allo.bank.service.store.InMemoryFinanceDataStore;
import com.allo.bank.strategy.HistoricalIdrUsdFetcher;
import com.allo.bank.strategy.LatestIdrRatesFetcher;
import com.allo.bank.strategy.SupportedCurrenciesFetcher;
import com.allo.bank.util.SpreadFactorCalculator;

@SpringBootTest(classes = StartupDataLoaderIntegrationTest.TestConfig.class)
@ActiveProfiles("test")
class StartupDataLoaderIntegrationTest {

    @Autowired
    private FinanceDataService financeDataService;

    @Test
    void shouldLoadAllResourcesIntoMemoryAtStartup() {
        assertThat(financeDataService.getByResourceType(LatestIdrRatesFetcher.RESOURCE_TYPE)).hasSize(1);
        assertThat(financeDataService.getByResourceType(HistoricalIdrUsdFetcher.RESOURCE_TYPE)).hasSize(1);
        assertThat(financeDataService.getByResourceType(SupportedCurrenciesFetcher.RESOURCE_TYPE)).hasSize(1);
    }

    @SpringBootConfiguration
    @Import({
        InMemoryFinanceDataStore.class,
        DefaultFinanceDataService.class,
        StartupDataLoader.class,
        LatestIdrRatesFetcher.class,
        HistoricalIdrUsdFetcher.class,
        SupportedCurrenciesFetcher.class,
        SpreadFactorCalculator.class
    })
    static class TestConfig {

        @Bean
        FrankfurterClient frankfurterClient() {
            return new StubFrankfurterClient();
        }

        @Bean
        AppProperties appProperties() {
            AppProperties properties = new AppProperties();
            properties.setGithubUsername("testuser");
            return properties;
        }
    }

    static class StubFrankfurterClient extends FrankfurterClient {

        StubFrankfurterClient() {
            super(null, null);
        }

        @Override
        public FrankfurterLatestResponse fetchLatestIdrRates() {
            FrankfurterLatestResponse response = new FrankfurterLatestResponse();
            response.setBase("IDR");
            response.setDate("2024-01-05");
            response.setAmount(1D);
            response.setRates(Map.of("USD", 0.000064D));
            return response;
        }

        @Override
        public FrankfurterHistoricalResponse fetchHistoricalIdrUsd() {
            FrankfurterHistoricalResponse response = new FrankfurterHistoricalResponse();
            response.setBase("IDR");
            response.setAmount(1D);
            response.setRates(Map.of("2024-01-01", Map.of("USD", 0.000064D)));
            return response;
        }

        @Override
        public Map<String, String> fetchSupportedCurrencies() {
            return Map.of("USD", "US Dollar");
        }
    }
}
