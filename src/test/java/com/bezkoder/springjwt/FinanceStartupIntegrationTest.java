package com.bezkoder.springjwt;

import com.bezkoder.springjwt.client.FrankfurterApiClient;
import com.bezkoder.springjwt.store.FinanceDataStore;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "github.username=testuser",
        "frankfurter.base-url=https://api.frankfurter.app"
})
class FinanceStartupIntegrationTest {

    //  JUnit + Spring: gunakan field injection
    @Autowired
    private FinanceDataStore store;

    //  Stub API client agar tidak call internet
    @TestConfiguration
    static class StubConfig {

        @Bean
        @Primary
        FrankfurterApiClient frankfurterApiClient() {
            return new FrankfurterApiClientStub();
        }
    }

    //  Stub dengan MUTABLE MAP (WAJIB)
    static class FrankfurterApiClientStub extends FrankfurterApiClient {

        FrankfurterApiClientStub() {
            super(null);
        }

        @Override
        public Map<String, Object> getLatestBaseIdr() {
            Map<String, Object> response = new HashMap<>();
            response.put("base", "IDR");
            response.put("date", "2026-01-04");

            Map<String, Object> rates = new HashMap<>();
            rates.put("USD", 0.000064);
            rates.put("EUR", 0.000058);

            response.put("rates", rates);
            return response;
        }

        @Override
        public Map<String, Object> getHistoricalIdrToUsd() {
            Map<String, Object> response = new HashMap<>();
            response.put("base", "IDR");

            Map<String, Object> rates = new HashMap<>();
            rates.put("2024-01-01", Map.of("USD", 0.000064));
            rates.put("2024-01-02", Map.of("USD", 0.000063));

            response.put("rates", rates);
            return response;
        }

        @Override
        public Map<String, String> getCurrencies() {
            Map<String, String> currencies = new HashMap<>();
            currencies.put("USD", "United States Dollar");
            currencies.put("IDR", "Indonesian Rupiah");
            return currencies;
        }
    }

    @Test
    void shouldInitializeInMemoryStoreOnStartup() {
        assertTrue(store.isInitialized(), "Store harus terinisialisasi saat startup");

        assertNotNull(store.getOrNull("latest_idr_rates"));
        assertNotNull(store.getOrNull("historical_idr_usd"));
        assertNotNull(store.getOrNull("supported_currencies"));
    }
}
