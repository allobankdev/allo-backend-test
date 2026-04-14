package com.allobank.test.runner;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.store.FinanceDataStore;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = {
        "finance.preload.enabled=true",
        "finance.preload.fail-fast=true"
})
@ActiveProfiles("test")
class FinanceDataPreloadRunnerIntegrationTest {

    @Autowired
    private FinanceDataStore financeDataStore;

    @Test
    void runnerShouldPreloadAllResourcesIntoStoreOnStartup() {
        List<Map<String, Object>> latest = financeDataStore.getByResourceType("latest_idr_rates");
        List<Map<String, Object>> historical = financeDataStore.getByResourceType("historical_idr_usd");
        List<Map<String, Object>> currencies = financeDataStore.getByResourceType("supported_currencies");

        assertEquals(1, latest.size());
        assertEquals(2, historical.size());
        assertEquals(2, currencies.size());
    }

    @Test
    void runnerLoadedDataShouldBeImmutableAtReadTime() {
        List<Map<String, Object>> latest = financeDataStore.getByResourceType("latest_idr_rates");
        assertThrows(UnsupportedOperationException.class, () -> latest.add(Map.of()));
    }

    @TestConfiguration
    static class StubFrankfurterClientConfiguration {

        @Bean
        @Primary
        FrankfurterClient frankfurterClient() {
            FrankfurterClient mockClient = Mockito.mock(FrankfurterClient.class);
            Mockito.when(mockClient.fetchLatestIdrRatesRaw()).thenReturn(Map.of(
                    "base", "IDR",
                    "date", "2024-01-05",
                    "rates", Map.of(
                            "USD", new BigDecimal("0.000064"),
                            "EUR", new BigDecimal("0.000057"))));
            Mockito.when(mockClient.fetchHistoricalIdrUsdRaw()).thenReturn(Map.of(
                    "rates", Map.of(
                            "2024-01-01", Map.of("USD", new BigDecimal("0.000063")),
                            "2024-01-02", Map.of("USD", new BigDecimal("0.000064")))));
            Mockito.when(mockClient.fetchSupportedCurrenciesRaw()).thenReturn(Map.of(
                    "USD", "United States Dollar",
                    "IDR", "Indonesian Rupiah"));
            return mockClient;
        }
    }
}
