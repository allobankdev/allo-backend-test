package com.api.allorestapi.strategy;

// import com.api.allorestapi.model.FinanceDataResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HistoricalIdrUsdFetcher Unit Tests")
class HistoricalIdrUsdFetcherTest {

    @Mock private WebClient webClient;
    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private HistoricalIdrUsdFetch fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new HistoricalIdrUsdFetch(
                webClient, "2024-01-01", "2024-01-05", "IDR", "USD");
    }

    @SuppressWarnings("unchecked")
    private void mockWebClient(Map<String, Object> responseBody) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(responseBody));
    }

    @Test
    @DisplayName("getResourceType() returns 'historical_idr_usd'")
    void getResourceType_returnsCorrectKey() {
        assertThat(fetcher.getResourceType()).isEqualTo("historical_idr_usd");
    }

    @Test
    @DisplayName("fetch() maps each date entry into the data array")
    @SuppressWarnings("unchecked")
    void fetch_mapsEachDateIntoDataArray() {
        Map<String, Object> rates = new LinkedHashMap<>();
        rates.put("2024-01-02", Map.of("USD", 0.000064));
        rates.put("2024-01-03", Map.of("USD", 0.000064));
        rates.put("2024-01-04", Map.of("USD", 0.000063));

        Map<String, Object> mockResponse = Map.of("rates", rates);
        mockWebClient(mockResponse);

        StepVerifier.create(fetcher.fetch())
                .assertNext(response -> {
                    assertThat(response.getResourceType()).isEqualTo("historical_idr_usd");
                    assertThat(response.getData()).hasSize(3);

                    // Verify first entry structure
                    Map<String, Object> first = (Map<String, Object>) response.getData().get(0);
                    assertThat(first).containsKey("date");
                    assertThat(first).containsKey("rates");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("fetch() sorts results by date ascending")
    @SuppressWarnings("unchecked")
    void fetch_sortsByDateAscending() {
        // Provide dates out of order
        Map<String, Object> rates = new LinkedHashMap<>();
        rates.put("2024-01-05", Map.of("USD", 0.000063));
        rates.put("2024-01-02", Map.of("USD", 0.000064));
        rates.put("2024-01-03", Map.of("USD", 0.000064));

        mockWebClient(Map.of("rates", rates));

        StepVerifier.create(fetcher.fetch())
                .assertNext(response -> {
                    List<Object> data = response.getData();
                    String first = (String) ((Map<String, Object>) data.get(0)).get("date");
                    String last  = (String) ((Map<String, Object>) data.get(2)).get("date");
                    assertThat(first).isEqualTo("2024-01-02");
                    assertThat(last).isEqualTo("2024-01-05");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("fetch() propagates WebClient errors")
    void fetch_propagatesError() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.error(new RuntimeException("Timeout")));

        StepVerifier.create(fetcher.fetch())
                .expectErrorMessage("Timeout")
                .verify();
    }
}
