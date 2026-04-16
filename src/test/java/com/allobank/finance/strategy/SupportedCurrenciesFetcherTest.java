package com.allobank.finance.strategy;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class SupportedCurrenciesFetcherTest {

    @Test
    void fetchDataReturnsSortedCurrencyRows() {
        AtomicReference<String> capturedUrl = new AtomicReference<>();
        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(WebClientStub.jsonClient("""
                {
                  "USD": "United States Dollar",
                  "IDR": "Indonesian Rupiah",
                  "EUR": "Euro"
                }
                """, capturedUrl));

        List<Map<String, Object>> result = fetcher.fetchData();

        assertThat(capturedUrl.get()).endsWith("/currencies");
        assertThat(result).extracting(row -> row.get("code"))
                .containsExactly("EUR", "IDR", "USD");
        assertThat(result.get(1)).containsEntry("name", "Indonesian Rupiah");
    }
}
