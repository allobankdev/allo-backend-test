package com.allobank.finance.runner;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.HistoricalResponse;
import com.allobank.finance.dto.LatestRateResponse;
import com.allobank.finance.service.InMemoryFinanceStore;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FinanceDataLoaderIntegrationTest {

    @TestConfiguration
    static class MockConfiguration {
        @Bean
        public FrankfurterClient frankfurterClient() {
            FrankfurterClient mock = Mockito.mock(FrankfurterClient.class);

            LatestRateResponse exchange =  new LatestRateResponse();
            exchange.setBaseCurrency("IDR");
            exchange.setDate("2024-01-01");
            exchange.setRates(Map.of("USD", BigDecimal.valueOf(0.000059)));

            HistoricalResponse historical =  new HistoricalResponse();
            historical.setBaseCurrency("IDR");

            Map<String, String> currencies = Map.of("USD", "United States Dollar");

            Mockito.when(mock.getLatestIdrRates()).thenReturn(exchange);
            Mockito.when(mock.getHistoricalIdrUsd()).thenReturn(historical);
            Mockito.when(mock.getCurrencies()).thenReturn(currencies);

            return mock;
        }
    }

    @Autowired
    private InMemoryFinanceStore inMemoryFinanceStore;

    @Autowired
    private FinanceDataLoader financeDataLoader;

    @Test
    void loadDataOnStartup() {

        // THEN
        assertNotNull(inMemoryFinanceStore.getData("latest_idr_rates"));
        assertNotNull(inMemoryFinanceStore.getData("historical_idr_usd"));
        assertNotNull(inMemoryFinanceStore.getData("supported_currencies"));
    }
}
