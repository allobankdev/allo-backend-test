package com.allobank.finance.idrrateaggregator.strategy.impl;

import com.allobank.finance.idrrateaggregator.client.FrankfurterClient;
import com.allobank.finance.idrrateaggregator.model.dto.FinanceResponse;
import com.allobank.finance.idrrateaggregator.model.dto.LatestRatesResponse;
import com.allobank.finance.idrrateaggregator.startegy.impl.LatestIdrRatesFetcher;
import com.allobank.finance.idrrateaggregator.util.SpreadCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LatestIdrRatesFetcherTest {

    @Mock
    private FrankfurterClient client;

    @InjectMocks
    private LatestIdrRatesFetcher fetcher;

    @Test
    void shouldReturnLatestRatesWithCalculatedSpread() {
        // given
        LatestRatesResponse response = new LatestRatesResponse();
        response.setBase("IDR");
        response.setDate("2026-02-03");
        response.setRates(Map.of("USD", 0.00006));

        when(client.getLatestIdrRates())
                .thenReturn(response);

        double expectedSpreadFactor =
                SpreadCalculator.calculateSpreadFactor("IlhamX7");

        double expectedUsdBuySpread =
                (1 / 0.00006) * (1 + expectedSpreadFactor);

        // when
        List<FinanceResponse> result = fetcher.fetch();

        // then
        assertThat(result).hasSize(5);

        assertThat(result)
                .extracting(FinanceResponse::getKey)
                .containsExactly(
                        "base",
                        "date",
                        "usd_rate",
                        "spread_factor",
                        "USD_BuySpread_IDR"
                );

        assertThat(result)
                .filteredOn(r -> r.getKey().equals("usd_rate"))
                .first()
                .extracting(FinanceResponse::getValue)
                .isEqualTo(0.00006);

        assertThat(result)
                .filteredOn(r -> r.getKey().equals("spread_factor"))
                .first()
                .extracting(FinanceResponse::getValue)
                .isEqualTo(expectedSpreadFactor);

        assertThat(result)
                .filteredOn(r -> r.getKey().equals("USD_BuySpread_IDR"))
                .first()
                .extracting(FinanceResponse::getValue)
                .isEqualTo(expectedUsdBuySpread);
    }

    @Test
    void shouldThrowExceptionWhenUsdRateMissing() {
        // given
        LatestRatesResponse response = new LatestRatesResponse();
        response.setRates(Map.of("EUR", 0.000051));

        when(client.getLatestIdrRates())
                .thenReturn(response);

        // then
        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("USD rate not found");
    }

    @Test
    void shouldReturnCorrectResourceType() {
        assertThat(fetcher.getResourceType())
                .isEqualTo("latest_idr_rates");
    }
}
