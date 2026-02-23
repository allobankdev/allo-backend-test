package com.allobank.finance.idrrateaggregator.strategy.impl;

import com.allobank.finance.idrrateaggregator.client.FrankfurterClient;
import com.allobank.finance.idrrateaggregator.model.dto.FinanceResponse;
import com.allobank.finance.idrrateaggregator.startegy.impl.SupportedCurrenciesFetcher;
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
public class SupportedCurrenciesFetcherTest {

    @Mock
    private FrankfurterClient client;

    @InjectMocks
    private SupportedCurrenciesFetcher fetcher;

    @Test
    void shouldReturnSupportedCurrenciesSortedByCode() {
        // given
        when(client.getSupportedCurrencies())
                .thenReturn(Map.of(
                        "USD", "United States Dollar",
                        "AUD", "Australian Dollar",
                        "IDR", "Indonesian Rupiah"
                ));

        // when
        List<FinanceResponse> result = fetcher.fetch();

        // then
        assertThat(result).hasSize(3);

        assertThat(result)
                .extracting(FinanceResponse::getKey)
                .containsExactly("AUD", "IDR", "USD");

        assertThat(result)
                .filteredOn(r -> r.getKey().equals("USD"))
                .first()
                .extracting(FinanceResponse::getValue)
                .isEqualTo("United States Dollar");
    }

    @Test
    void shouldThrowExceptionWhenCurrenciesNotAvailable() {
        // given
        when(client.getSupportedCurrencies())
                .thenReturn(null);

        // then
        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Supported currencies not available");
    }

    @Test
    void shouldReturnCorrectResourceType() {
        assertThat(fetcher.getResourceType())
                .isEqualTo("supported_currencies");
    }
}
