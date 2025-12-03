package com.example.frankfurter.strategy;

import com.example.frankfurter.client.FrankfurterClient;
import com.example.frankfurter.dto.FrankfurterTimeseriesResponse;
import com.example.frankfurter.dto.HistoricalIdrUsdDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HistoricalIdrUsdFetcherTest {

    @Test
    void shouldMapTimeseriesResponseToHistoricalDtoList() {
        // Arrange
        FrankfurterClient client = mock(FrankfurterClient.class);
        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(client);

        FrankfurterTimeseriesResponse resp = new FrankfurterTimeseriesResponse();
        resp.setBase("IDR");
        resp.setStart_date("2024-01-01");
        resp.setEnd_date("2024-01-05");

        // rates: { "2024-01-01": { "USD": 0.00006 }, "2024-01-02": { "USD": 0.00007 } }
        resp.setRates(Map.of(
                "2024-01-01", Map.of("USD", 0.00006),
                "2024-01-02", Map.of("USD", 0.00007)
        ));

        when(client.getHistoricalIdrUsd(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(resp);

        // Act
        List<?> result = fetcher.fetchData();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isInstanceOf(HistoricalIdrUsdDto.class);

        HistoricalIdrUsdDto first = (HistoricalIdrUsdDto) result.get(0);
        HistoricalIdrUsdDto second = (HistoricalIdrUsdDto) result.get(1);

        assertThat(first.getDate()).isEqualTo("2024-01-01");
        assertThat(first.getUsdRate()).isEqualTo(0.00006);

        assertThat(second.getDate()).isEqualTo("2024-01-02");
        assertThat(second.getUsdRate()).isEqualTo(0.00007);
    }
}
