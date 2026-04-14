package com.thasya.frankfurter.strategy;

import com.thasya.frankfurter.client.FrankfurterClient;
import com.thasya.frankfurter.dto.CurrencyDto;
import com.thasya.frankfurter.strategy.SupportedCurrenciesFetcher;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SupportedCurrenciesFetcherTest {

    @Test
    void shouldMapCurrencyMapToDtoList() {
        // Arrange
        FrankfurterClient client = mock(FrankfurterClient.class);
        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(client);

        when(client.getCurrencies()).thenReturn(Map.of(
                "USD", "United States Dollar",
                "EUR", "Euro",
                "IDR", "Indonesian Rupiah"
        ));

        // Act
        List<?> result = fetcher.fetchData();

        // Assert
        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isInstanceOf(CurrencyDto.class);

        // Bisa cek salah satu item
        CurrencyDto usd = (CurrencyDto) result.stream()
                .map(CurrencyDto.class::cast)
                .filter(c -> c.getCode().equals("USD"))
                .findFirst()
                .orElseThrow();

        assertThat(usd.getDescription()).isEqualTo("United States Dollar");
    }
}
