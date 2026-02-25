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
class LatestIdrRatesFetcherTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private SpreadCalculator spreadCalculator;
    private LatestIdrRatesFetcher fetcher;

    @BeforeEach
    void setUp() {
        // username "thaufaniqbal" → sum=1264 → factor=0.00264
        spreadCalculator = new SpreadCalculator("thaufaniqbal");
        fetcher = new LatestIdrRatesFetcher(webClient, spreadCalculator);
    }

    @Test
    void fetch_shouldReturnLatestIdrRatesWithSpread() {
        // Arrange
        FrankfurterDto.LatestRatesResponse mockResponse = new FrankfurterDto.LatestRatesResponse();
        mockResponse.setAmount("1");
        mockResponse.setBase("IDR");
        mockResponse.setDate("2024-01-05");
        mockResponse.setRates(Map.of("USD", BigDecimal.valueOf(0.000064), "EUR", BigDecimal.valueOf(0.000059)));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterDto.LatestRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        // Act
        FinanceDataResponse result = fetcher.fetch();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getResourceType()).isEqualTo("latest_idr_rates");
        assertThat(result.getData()).isNotNull();
        assertThat(result.getUsdBuySpreadIdr()).isNotNull();
        assertThat(result.getSpreadFactor()).isEqualTo(0.00264);
        assertThat(result.getFetchedAt()).isNotNull();

        // Verify spread calculation: (1 / 0.000064) * (1 + 0.00264) = 15666.75
        double expectedSpread = (1.0 / 0.000064) * (1.0 + 0.00264);
        assertThat(result.getUsdBuySpreadIdr()).isCloseTo(expectedSpread, within(0.01));
    }

    @Test
    void fetch_shouldThrowWhenApiReturnsNull() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterDto.LatestRatesResponse.class))
                .thenReturn(Mono.empty());

        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to fetch latest IDR rates");
    }

    @Test
    void fetch_shouldThrowWhenUsdRateIsMissing() {
        FrankfurterDto.LatestRatesResponse mockResponse = new FrankfurterDto.LatestRatesResponse();
        mockResponse.setBase("IDR");
        mockResponse.setRates(Map.of("EUR", BigDecimal.valueOf(0.000059))); // no USD

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterDto.LatestRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to fetch latest IDR rates");
    }

    @Test
    void getResourceType_shouldReturnCorrectKey() {
        assertThat(fetcher.getResourceType()).isEqualTo("latest_idr_rates");
    }
}
