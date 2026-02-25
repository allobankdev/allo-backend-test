package com.allobank.finance.fetcher;

import com.allobank.finance.dto.FinanceDataResponse;
import com.allobank.finance.dto.FrankfurterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdFetcherTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private HistoricalIdrUsdFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new HistoricalIdrUsdFetcher(webClient);
    }

    @Test
    void fetch_shouldReturnHistoricalRates() {
        // Arrange
        FrankfurterDto.HistoricalRatesResponse mockResponse = new FrankfurterDto.HistoricalRatesResponse();
        mockResponse.setBase("IDR");
        mockResponse.setStartDate("2024-01-01");
        mockResponse.setEndDate("2024-01-05");
        mockResponse.setRates(Map.of(
                "2024-01-02", Map.of("USD", BigDecimal.valueOf(0.000064)),
                "2024-01-03", Map.of("USD", BigDecimal.valueOf(0.000065)),
                "2024-01-05", Map.of("USD", BigDecimal.valueOf(0.000063))
        ));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterDto.HistoricalRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        // Act
        FinanceDataResponse result = fetcher.fetch();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getResourceType()).isEqualTo("historical_idr_usd");
        assertThat(result.getData()).isInstanceOf(FrankfurterDto.HistoricalRatesResponse.class);
        assertThat(result.getUsdBuySpreadIdr()).isNull(); // no spread for historical
        assertThat(result.getFetchedAt()).isNotNull();

        FrankfurterDto.HistoricalRatesResponse data =
                (FrankfurterDto.HistoricalRatesResponse) result.getData();
        assertThat(data.getStartDate()).isEqualTo("2024-01-01");
        assertThat(data.getEndDate()).isEqualTo("2024-01-05");
        assertThat(data.getRates()).hasSize(3);
    }

    @Test
    void fetch_shouldThrowWhenApiReturnsNull() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterDto.HistoricalRatesResponse.class))
                .thenReturn(Mono.empty());

        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to fetch historical IDR/USD rates");
    }

    @Test
    void getResourceType_shouldReturnCorrectKey() {
        assertThat(fetcher.getResourceType()).isEqualTo("historical_idr_usd");
    }
}
