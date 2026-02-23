package com.allobank.finance.idrrateaggregator.strategy.impl;

import com.allobank.finance.idrrateaggregator.client.FrankfurterClient;
import com.allobank.finance.idrrateaggregator.model.dto.FinanceResponse;
import com.allobank.finance.idrrateaggregator.model.dto.HistoricalRatesResponse;
import com.allobank.finance.idrrateaggregator.startegy.impl.HistoricalIdrUsdFetcher;
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
public class HistoricalIdrUsdFetcherTest {

    @Mock
    private FrankfurterClient client;

    @InjectMocks
    private HistoricalIdrUsdFetcher fetcher;

    @Test
    void shouldReturnHistoricalUsdRatesMappedByDate() {
        // given
        HistoricalRatesResponse response = new HistoricalRatesResponse();
        response.setRates(Map.of(
                "2024-01-01", Map.of("USD", 0.000065),
                "2024-01-02", Map.of("USD", 0.000064)
        ));

        when(client.getHistoricalIdrUsdRates())
                .thenReturn(response);

        // when
        List<FinanceResponse> result = fetcher.fetch();

        // then
        assertThat(result).hasSize(2);

        assertThat(result)
                .extracting(FinanceResponse::getKey)
                .containsExactlyInAnyOrder("2024-01-01", "2024-01-02");

        assertThat(result)
                .extracting(FinanceResponse::getValue)
                .contains(0.000065, 0.000064);
    }

    @Test
    void shouldThrowExceptionWhenUsdRateMissingForAnyDate() {
        // given
        HistoricalRatesResponse response = new HistoricalRatesResponse();
        response.setRates(Map.of(
                "2024-01-01", Map.of("EUR", 0.000059) // USD missing
        ));

        when(client.getHistoricalIdrUsdRates())
                .thenReturn(response);

        // then
        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("USD rate missing for date 2024-01-01");
    }

    @Test
    void shouldReturnCorrectResourceType() {
        assertThat(fetcher.getResourceType())
                .isEqualTo("historical_idr_usd");
    }
}
