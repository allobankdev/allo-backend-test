package com.allobank.financeaggregator;

import static org.assertj.core.api.Assertions.assertThat;

import com.allobank.financeaggregator.dto.HistoricalIdrUsdDto;
import com.allobank.financeaggregator.dto.LatestIdrRatesDto;
import com.allobank.financeaggregator.dto.SupportedCurrenciesDto;
import com.allobank.financeaggregator.model.FinanceDataItem;
import com.allobank.financeaggregator.model.HistoricalRatesResponse;
import com.allobank.financeaggregator.model.LatestRatesResponse;
import com.allobank.financeaggregator.service.FinanceDataStore;
import com.allobank.financeaggregator.service.FrankfurterClient;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
class FinanceDataLoaderIntegrationTest {

    @Autowired
    private FinanceDataStore dataStore;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("frankfurter.github-username", () -> "testuser");
        registry.add("finance.historical.start-date", () -> "2024-01-01");
        registry.add("finance.historical.end-date", () -> "2024-01-05");
        registry.add("finance.historical.from", () -> "IDR");
        registry.add("finance.historical.to", () -> "USD");
    }

    @Test
    void dataLoadedAtStartup() {
        assertThat(dataStore.isLoaded()).isTrue();
        FinanceDataItem<?> latest = dataStore.get("latest_idr_rates").get(0);
        FinanceDataItem<?> historical = dataStore.get("historical_idr_usd").get(0);
        FinanceDataItem<?> currencies = dataStore.get("supported_currencies").get(0);

        assertThat(latest.data()).isInstanceOf(LatestIdrRatesDto.class);
        assertThat(historical.data()).isInstanceOf(HistoricalIdrUsdDto.class);
        assertThat(currencies.data()).isInstanceOf(SupportedCurrenciesDto.class);
    }

    @TestConfiguration
    static class StubClientConfig {

        @Bean
        @Primary
        FrankfurterClient frankfurterClient() {
            LatestRatesResponse latest = new LatestRatesResponse(
                    new BigDecimal("1.0"),
                    "IDR",
                    "2024-01-05",
                    Map.of("USD", new BigDecimal("0.000065"))
            );
            HistoricalRatesResponse historical = new HistoricalRatesResponse(
                    new BigDecimal("1.0"),
                    "IDR",
                    "2024-01-01",
                    "2024-01-05",
                    Map.of("2024-01-01", Map.of("USD", new BigDecimal("0.000065")))
            );
            Map<String, String> currencies = Map.of(
                    "USD", "United States Dollar",
                    "IDR", "Indonesian Rupiah"
            );

            return new StubFrankfurterClient(latest, historical, currencies);
        }
    }

    private static class StubFrankfurterClient extends FrankfurterClient {

        private final LatestRatesResponse latest;
        private final HistoricalRatesResponse historical;
        private final Map<String, String> currencies;

        private StubFrankfurterClient(
                LatestRatesResponse latest,
                HistoricalRatesResponse historical,
                Map<String, String> currencies
        ) {
            super(WebClient.builder().build());
            this.latest = latest;
            this.historical = historical;
            this.currencies = currencies;
        }

        @Override
        public <T> T get(String path, Class<T> responseType) {
            if (path.startsWith("/latest")) {
                return responseType.cast(latest);
            }
            if (path.startsWith("/2024-01-01..2024-01-05")) {
                return responseType.cast(historical);
            }
            throw new IllegalArgumentException("Unexpected path: " + path);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(String path, ParameterizedTypeReference<T> responseType) {
            if (path.equals("/currencies")) {
                return (T) currencies;
            }
            throw new IllegalArgumentException("Unexpected path: " + path);
        }
    }
}
