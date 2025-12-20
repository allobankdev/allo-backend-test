package com.allo_backend_test.service;

import com.allo_backend_test.strategy.IDRDataFetcher;
import com.allo_backend_test.strategy.ResourceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(FinanceDataInitializerIT.TestConfig.class)
class FinanceDataInitializerIT {

    @Autowired
    private FinanceDataStore dataStore;

    @Test
    void shouldInitializeAllFinanceDataOnStartup() {
        assertThat(dataStore.contains(ResourceTypes.LATEST_IDR_RATES)).isTrue();
        assertThat(dataStore.contains(ResourceTypes.HISTORICAL_IDR_USD)).isTrue();
        assertThat(dataStore.contains(ResourceTypes.SUPPORTED_CURRENCIES)).isTrue();

        assertThat(dataStore.getByResourceType(ResourceTypes.LATEST_IDR_RATES)).isNotEmpty();
        assertThat(dataStore.getByResourceType(ResourceTypes.HISTORICAL_IDR_USD)).isNotEmpty();
        assertThat(dataStore.getByResourceType(ResourceTypes.SUPPORTED_CURRENCIES)).isNotEmpty();
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        IDRDataFetcher latestFetcher() {
            return new FakeFetcher(ResourceTypes.LATEST_IDR_RATES);
        }

        @Bean
        IDRDataFetcher historicalFetcher() {
            return new FakeFetcher(ResourceTypes.HISTORICAL_IDR_USD);
        }

        @Bean
        IDRDataFetcher currenciesFetcher() {
            return new FakeFetcher(ResourceTypes.SUPPORTED_CURRENCIES);
        }
    }

}
