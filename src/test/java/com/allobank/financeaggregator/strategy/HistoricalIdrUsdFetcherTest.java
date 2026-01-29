package com.allobank.financeaggregator.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.allobank.financeaggregator.config.HistoricalProperties;
import com.allobank.financeaggregator.dto.HistoricalIdrUsdDto;
import com.allobank.financeaggregator.model.HistoricalRatesResponse;
import com.allobank.financeaggregator.service.FrankfurterClient;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

class HistoricalIdrUsdFetcherTest {

    @Test
    void fetchDataUsesConfiguredRange() {
        HistoricalRatesResponse response = new HistoricalRatesResponse(
                new BigDecimal("1.0"),
                "IDR",
                "2024-01-01",
                "2024-01-05",
                Map.of(
                        "2023-12-29", Map.of("USD", new BigDecimal("0.000065")),
                        "2024-01-02", Map.of("USD", new BigDecimal("0.000064"))
                )
        );
        StubFrankfurterClient client = new StubFrankfurterClient(response);

        HistoricalProperties properties = new HistoricalProperties();
        properties.setStartDate(java.time.LocalDate.of(2024, 1, 1));
        properties.setEndDate(java.time.LocalDate.of(2024, 1, 5));
        properties.setFrom("IDR");
        properties.setTo("USD");

        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(client, properties);
        HistoricalIdrUsdDto payload = fetcher.fetchData();

        assertThat(client.lastPath()).isEqualTo("/2024-01-01..2024-01-05?from=IDR&to=USD");
        assertThat(payload.startDate()).isEqualTo("2024-01-01");
        assertThat(payload.endDate()).isEqualTo("2024-01-05");
        assertThat(payload.rates()).doesNotContainKey("2023-12-29");
        assertThat(payload.rates()).containsKey("2024-01-02");
    }

    private static class StubFrankfurterClient extends FrankfurterClient {

        private final HistoricalRatesResponse response;
        private String lastPath;

        private StubFrankfurterClient(HistoricalRatesResponse response) {
            super(WebClient.builder().build());
            this.response = response;
        }

        @Override
        public <T> T get(String path, Class<T> responseType) {
            lastPath = path;
            if (path.startsWith("/2024-01-01..2024-01-05")) {
                return responseType.cast(response);
            }
            throw new IllegalArgumentException("Unexpected path: " + path);
        }

        String lastPath() {
            return lastPath;
        }
    }
}
