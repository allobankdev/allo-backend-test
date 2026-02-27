package com.amri.apiintegration.application.runner;

import com.amri.apiintegration.application.cache.FinanceDataInMemoryStore;
import com.amri.apiintegration.application.port.CurrencyRatesGateway;
import com.amri.apiintegration.dto.frankfurter.CurrenciesDto;
import com.amri.apiintegration.dto.frankfurter.HistoricalRatesDto;
import com.amri.apiintegration.dto.frankfurter.LatestRatesDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "app.github-username=AmriGIT",
        "external.frankfurter.base-url=https://api.frankfurter.app"
})
class FinanceDataLoaderRunnerIntegrationTest {

    @TestConfiguration
    static class MockGatewayConfig {
        @Bean
        @Primary
        CurrencyRatesGateway currencyRatesGateway() {
            return new CurrencyRatesGateway() {
                @Override
                public LatestRatesDto getLatestRates(String base) {
                    return new LatestRatesDto(base, "2026-02-26", Map.of("USD", new BigDecimal("0.00006150")), null);
                }

                @Override
                public HistoricalRatesDto getHistoricalRates(String startDate, String endDate, String from, String to) {
                    return new HistoricalRatesDto(
                            BigDecimal.ONE,
                            from,
                            Map.of(startDate, Map.of(to, new BigDecimal("0.000064")))
                    );
                }

                @Override
                public CurrenciesDto getCurrencies() {
                    return new CurrenciesDto(Map.of("USD", "United States Dollar"));
                }
            };
        }
    }

    @Autowired
    private FinanceDataInMemoryStore inMemoryStore;

    @Test
    void startupRunner_shouldLoadAllResourcesIntoImmutableStore() {
        Map<String, ?> snapshot = inMemoryStore.snapshot();

        assertEquals(3, snapshot.size());
        assertTrue(snapshot.containsKey("latest_idr_rates"));
        assertTrue(snapshot.containsKey("historical_idr_usd"));
        assertTrue(snapshot.containsKey("supported_currencies"));
    }
}
