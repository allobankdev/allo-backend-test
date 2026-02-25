package com.allobank.finance.strategy;

import com.allobank.finance.client.model.HistoricalRate;
import com.allobank.finance.model.HistoricalRateData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricalIDRUSDFetcherTest {

    @Mock
    private RestClient restClient;

    private HistoricalIDRUSDFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new HistoricalIDRUSDFetcher(restClient);
    }

    @Test
    void shouldReturnCorrectResourceType() {
        assertThat(fetcher.getResourceType()).isEqualTo("historical_idr_usd");
    }

    @Test
    void shouldFetchAndTransformHistoricalRates() {
        // Arrange
        HistoricalRate mockResponse = new HistoricalRate(
                BigDecimal.ONE,
                "IDR",
                "2024-01-01",
                "2024-01-05",
                Map.of(
                        "2024-01-01", Map.of("USD", new BigDecimal("0.000063")),
                        "2024-01-02", Map.of("USD", new BigDecimal("0.000064")),
                        "2024-01-03", Map.of("USD", new BigDecimal("0.000065"))
                )
        );

        mockRestClient(mockResponse);

        // Act
        HistoricalRateData result = fetcher.fetchData();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.base()).isEqualTo("IDR");
        assertThat(result.startDate()).isEqualTo("2024-01-01");
        assertThat(result.endDate()).isEqualTo("2024-01-05");
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(result.rates()).hasSize(3);
        assertThat(result.rates().get("2024-01-01").get("USD")).isEqualByComparingTo("0.000063");
    }

    @SuppressWarnings("unchecked")
    private void mockRestClient(HistoricalRate response) {
        var uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(HistoricalRate.class)).thenReturn(response);
    }
}
