package com.allobank.finance.strategy;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class LatestIdrRatesFetcherTest {

    @Test
    void fetchDataReturnsLatestRatesWithPersonalizedUsdBuySpread() {
        AtomicReference<String> capturedUrl = new AtomicReference<>();
        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(
                WebClientStub.jsonClient("""
                        {
                          "amount": 1.0,
                          "base": "IDR",
                          "date": "2024-01-05",
                          "rates": {
                            "USD": 0.000064
                          }
                        }
                        """, capturedUrl),
                "BiasCoder");

        Map<String, Object> result = fetcher.fetchData().get(0);

        assertThat(capturedUrl.get()).contains("/latest").contains("base=IDR");
        assertThat(result).containsEntry("github_username", "biascoder");
        assertThat(result).containsEntry("spread_factor", 0.0094);
        assertThat((Double) result.get("USD_BuySpread_IDR")).isCloseTo(15771.875, withinPercentage(0.0001));
    }

    @Test
    void calculateSpreadFactorUsesLowercaseUsernameAsciiSum() {
        assertThat(LatestIdrRatesFetcher.calculateSpreadFactor("BiasCoder")).isEqualTo(0.0094);
    }

    private static org.assertj.core.data.Percentage withinPercentage(double percentage) {
        return org.assertj.core.data.Percentage.withPercentage(percentage);
    }
}
