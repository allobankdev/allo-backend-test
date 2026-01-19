package com.example.allo.strategy;

import com.example.allo.client.FrankfurterClient;
import com.example.allo.dto.HistoricalRatesResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdFetcherTest {

    @Mock
    private FrankfurterClient client;

    @InjectMocks
    private HistoricalIdrUsdFetcher fetcher;

    @Test
    void shouldReturnCorrectResourceType() {
        // when
        String resourceType = fetcher.getResourceType();

        // then
        assertThat(resourceType).isEqualTo("historical_idr_usd");
    }

    @Test
    void shouldFetchHistoricalIdrUsdRatesFromClient() {
        // given
        HistoricalRatesResponse historicalRatesResponse = new HistoricalRatesResponse();
        historicalRatesResponse.setRates(Map.of("2024-01-01", Map.of("USD", 0.000064)));

        when(client.getHistoricalRates(
                "2024-01-01",
                "2024-01-05",
                "IDR",
                "USD"
        )).thenReturn(historicalRatesResponse);

        // when
        Object result = fetcher.fetch();

        // then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(HistoricalRatesResponse.class);

        verify(client, times(1)).getHistoricalRates(
                "2024-01-01",
                "2024-01-05",
                "IDR",
                "USD"
        );
        verifyNoMoreInteractions(client);
    }
}