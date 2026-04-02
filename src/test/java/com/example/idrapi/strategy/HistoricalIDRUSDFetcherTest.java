package com.example.idrapi.strategy;

import com.example.idrapi.config.FrankfurterProperties;
import com.example.idrapi.dto.HistoricalRatesResponse;
import com.example.idrapi.strategy.impl.HistoricalIDRUSDFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HistoricalIDRUSDFetcher Unit Tests")
class HistoricalIDRUSDFetcherTest {

    @Mock private WebClient webClient;
    @Mock private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private HistoricalIDRUSDFetcher fetcher;

    @BeforeEach
    void setUp() {
        FrankfurterProperties properties = new FrankfurterProperties();
        properties.setBaseUrl("https://api.frankfurter.app");
        FrankfurterProperties.Historical historical = new FrankfurterProperties.Historical();
        historical.setStartDate("2024-01-01");
        historical.setEndDate("2024-01-05");
        properties.setHistorical(historical);

        fetcher = new HistoricalIDRUSDFetcher(webClient, properties);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("fetch: flattens response into one record per date")
    void fetch_flattensIntoPerDateRecords() {
        // Arrange
        HistoricalRatesResponse mockResponse = new HistoricalRatesResponse();
        mockResponse.setBase("IDR");
        mockResponse.setStartDate("2024-01-01");
        mockResponse.setEndDate("2024-01-05");
        mockResponse.setRates(Map.of(
                "2024-01-02", Map.of("USD", 0.000064),
                "2024-01-03", Map.of("USD", 0.000065),
                "2024-01-04", Map.of("USD", 0.000063),
                "2024-01-05", Map.of("USD", 0.000066)
        ));

        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec)
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD");
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(responseSpec).when(responseSpec).onStatus(any(), any());
        doReturn(Mono.just(mockResponse)).when(responseSpec)
                .bodyToMono(HistoricalRatesResponse.class);

        // Act
        List<Map<String, Object>> results = fetcher.fetch();

        // Assert
        assertThat(results).hasSize(4);
        results.forEach(record -> {
            assertThat(record).containsKeys("date", "base", "USD", "startDate", "endDate");
            assertThat(record.get("base")).isEqualTo("IDR");
        });
    }

    @Test
    @DisplayName("getResourceType: returns correct key")
    void getResourceType_returnsCorrectKey() {
        assertThat(fetcher.getResourceType()).isEqualTo("historical_idr_usd");
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("fetch: throws IllegalStateException on null response")
    void fetch_throwsOnNullResponse() {
        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec)
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD");
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(responseSpec).when(responseSpec).onStatus(any(), any());
        doReturn(Mono.empty()).when(responseSpec).bodyToMono(HistoricalRatesResponse.class);

        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("null response");
    }
}
