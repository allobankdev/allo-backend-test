package com.allobank.finance.strategy;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class HistoricalIdrUsdFetcherTest {

    @Test
    void fetchDataReturnsSortedRowsForHistoricalRates() {
        AtomicReference<String> capturedUrl = new AtomicReference<>();
        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(WebClientStub.jsonClient("""
                {
                  "amount": 1.0,
                  "base": "IDR",
                  "start_date": "2024-01-01",
                  "end_date": "2024-01-05",
                  "rates": {
                    "2024-01-02": { "USD": 0.000065 },
                    "2024-01-01": { "USD": 0.000064 }
                  }
                }
                """, capturedUrl));

        List<Map<String, Object>> result = fetcher.fetchData();

        assertThat(capturedUrl.get())
                .contains("/2024-01-01..2024-01-05")
                .contains("from=IDR")
                .contains("to=USD");
        assertThat(result).extracting(row -> row.get("date"))
                .containsExactly("2024-01-01", "2024-01-02");
        assertThat(result.get(0)).containsEntry("base", "IDR");
    }
}
