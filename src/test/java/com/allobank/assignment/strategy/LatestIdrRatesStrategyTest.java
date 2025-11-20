package com.allobank.assignment.strategy;

import com.allobank.assignment.client.FrankfurterApiClient;
import com.allobank.assignment.config.FrankfurterApiProperties;
import com.allobank.assignment.exception.ExternalServiceException;
import com.allobank.assignment.model.FinanceDataResponse;
import com.allobank.assignment.model.LatestRatesAggregation;
import com.allobank.assignment.model.LatestRatesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class LatestIdrRatesStrategyTest {
    private FrankfurterApiClient apiClient;
    private FrankfurterApiProperties properties;
    private LatestIdrRatesStrategy strategy;

    @BeforeEach
    void setUp() {
        apiClient = mock(FrankfurterApiClient.class);
        properties = new FrankfurterApiProperties();
        properties.setGithubUsername("praydwi24");
        strategy = new LatestIdrRatesStrategy(apiClient, properties);
    }

    @Test
    void shouldCalculateSpreadAndWrapResponse() {
        LatestRatesResponse response = new LatestRatesResponse(
                BigDecimal.ONE,
                "IDR",
                LocalDate.of(2024, 1, 5),
                Map.of("USD", new BigDecimal("0.000064"), "EUR", new BigDecimal("0.000059")));
        given(apiClient.getLatestRates("IDR")).willReturn(response);

        FinanceDataResponse result = strategy.fetch();

        assertThat(result.resourceType()).isEqualTo("latest_idr_rates");
        assertThat(result.payload()).isInstanceOf(LatestRatesAggregation.class);
        LatestRatesAggregation aggregation = (LatestRatesAggregation) result.payload();
        BigDecimal expected = BigDecimal.ONE
                .divide(response.rates().get("USD"), 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.ONE.add(new BigDecimal("0.00870")))
                .setScale(6, RoundingMode.HALF_UP);
        assertThat(aggregation.usdBuySpreadIdr()).isEqualByComparingTo(expected);
        assertThat(aggregation.rates()).containsEntry("USD", new BigDecimal("0.000064"));
        assertThat(result.fetchedAt()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    void shouldFailWhenUsdRateMissing() {
        LatestRatesResponse response = new LatestRatesResponse(
                BigDecimal.ONE,
                "IDR",
                LocalDate.of(2024, 1, 5),
                Map.of("EUR", new BigDecimal("0.000059")));
        given(apiClient.getLatestRates("IDR")).willReturn(response);

        assertThatThrownBy(strategy::fetch)
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("missing USD rate");
    }
}
