package com.allobank.finance.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("LatestIDRRatesStrategy Unit Tests")
class LatestIDRRatesStrategyTest {

    @Test
    @DisplayName("Spread factor untuk 'patarebenezer' harus 0.00384")
    void shouldCalculateCorrectSpreadFactor() {
        double actual = LatestIDRRatesStrategy.calculateSpreadFactor("patarebenezer");
        assertThat(actual).isEqualTo(0.00384, within(0.000001));
    }

    @Test
    @DisplayName("Spread factor harus case-insensitive")
    void shouldCalculateSpreadFactorCaseInsensitively() {
        double lower = LatestIDRRatesStrategy.calculateSpreadFactor("patarebenezer");
        double upper = LatestIDRRatesStrategy.calculateSpreadFactor("PATAREBENEZER");
        assertThat(lower).isEqualTo(upper, within(0.000001));
    }

    @Test
    @DisplayName("Username kosong harus throw IllegalArgumentException")
    void shouldThrowForBlankUsername() {
        assertThatThrownBy(() ->
                LatestIDRRatesStrategy.calculateSpreadFactor(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Username null harus throw IllegalArgumentException")
    void shouldThrowForNullUsername() {
        assertThatThrownBy(() ->
                LatestIDRRatesStrategy.calculateSpreadFactor(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("USD_BuySpread_IDR harus lebih besar dari 1/Rate_USD")
    void usdBuySpreadShouldBeGreaterThanBaseRate() {
        // Buat strategy dengan dummy WebClient (tidak dipanggil di test ini)
        LatestIDRRatesStrategy strategy = new LatestIDRRatesStrategy(
                null, "patarebenezer");

        double rateUSD = 0.000062;
        double baseConversion = 1.0 / rateUSD;
        double spreadResult = strategy.calculateUsdBuySpreadIDR(rateUSD);

        assertThat(spreadResult).isGreaterThan(baseConversion);
    }

    @Test
    @DisplayName("Rate_USD nol harus throw ArithmeticException")
    void shouldThrowWhenRateUSDIsZero() {
        LatestIDRRatesStrategy strategy = new LatestIDRRatesStrategy(
                null, "patarebenezer");

        assertThatThrownBy(() -> strategy.calculateUsdBuySpreadIDR(0.0))
                .isInstanceOf(ArithmeticException.class);
    }

    @Test
    @DisplayName("Spread factor harus antara 0 dan 0.00999")
    void spreadFactorShouldBeWithinValidRange() {
        double factor = LatestIDRRatesStrategy.calculateSpreadFactor("patarebenezer");
        assertThat(factor).isBetween(0.0, 0.00999);
    }

    @Test
    @DisplayName("getResourceType() harus return 'latest_idr_rates'")
    void shouldReturnCorrectResourceType() {
        LatestIDRRatesStrategy strategy = new LatestIDRRatesStrategy(
                null, "patarebenezer");
        assertThat(strategy.getResourceType()).isEqualTo("latest_idr_rates");
    }

    @Test
    @DisplayName("getSpreadFactor() harus return 0.00384")
    void shouldExposeCorrectSpreadFactor() {
        LatestIDRRatesStrategy strategy = new LatestIDRRatesStrategy(
                null, "patarebenezer");
        assertThat(strategy.getSpreadFactor()).isEqualTo(0.00384, within(0.000001));
    }
}