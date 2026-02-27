package com.allobank.finance.runner;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.HistoricalIdrUsdDto;
import com.allobank.finance.dto.LatestIdrRatesDto;
import com.allobank.finance.dto.SupportedCurrenciesDto;
import com.allobank.finance.store.FinanceDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class FinanceDataLoaderTest {

    @Autowired
    private FinanceDataStore dataStore;

    @TestConfiguration
    static class MockConfig {

        @Bean
        FrankfurterClient frankfurterClient() {
            FrankfurterClient mock = mock(FrankfurterClient.class);

            when(mock.getHistoricalRates()).thenReturn(createHistoricalRatesDto());
            when(mock.getLatestRates()).thenReturn(createLatestRatesDto());
            when(mock.getSupportedCurrencies()).thenReturn(createSupportedCurrenciesDto());

            return mock;
        }

        private static LatestIdrRatesDto createLatestRatesDto() {
            LatestIdrRatesDto dto = new LatestIdrRatesDto();
            dto.setRates(Map.of("USD", 15000.0));
            return dto;
        }

        private static HistoricalIdrUsdDto createHistoricalRatesDto() {
            HistoricalIdrUsdDto dto = new HistoricalIdrUsdDto();
            dto.setBase("IDR");
            dto.setStartDate("2024-01-01");
            dto.setEndDate("2024-01-05");
            dto.setRates(Map.of("2024-01-01", Map.of("USD", 15000.0)));
            return dto;
        }

        private static SupportedCurrenciesDto createSupportedCurrenciesDto() {
            SupportedCurrenciesDto dto = new SupportedCurrenciesDto();
            dto.put("USD", "United States Dollar");
            dto.put("IDR", "Indonesian Rupiah");
            return dto;
        }
    }

    @Test
    void shouldLoadDataIntoStoreWhenApplicationStarts() {
        assertNotNull(dataStore.getData("historical_idr_usd"));
        assertNotNull(dataStore.getData("latest_idr_rates"));
        assertNotNull(dataStore.getData("supported_currencies"));
    }

    @Test
    void dataStoreShouldBeImmutable() {
        HistoricalIdrUsdDto dto = (HistoricalIdrUsdDto) dataStore.getData("historical_idr_usd");
        Map<String, Map<String, Double>> rates = dto.getRates();

        assertThrows(UnsupportedOperationException.class, () -> rates.put("2024-01-06", Map.of("USD", 16000.0)));
    }
}