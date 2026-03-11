package com.api.allorestapi.strategy;

// import com.api.allorestapi.model.FinanceDataResponse;
import com.api.allorestapi.service.SpreadCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LatestIdrRatesFetcher Unit Tests")
class LatestIdrRatesFetchTest {

    @Mock private WebClient webClient;
    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private SpreadCalculator spreadCalculator;
    private LatestIdrRatesFetcher fetcher;

    @BeforeEach
    void setUp() {
        // Use known username so spread factor is deterministic
        spreadCalculator = new SpreadCalculator("mrafi68"); // sum=637, factor=0.00637
        fetcher = new LatestIdrRatesFetcher(webClient, spreadCalculator);
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
    @DisplayName("getResourceType() returns 'latest_idr_rates'")
    void getResourceType_returnsCorrectKey() {
        assertThat(fetcher.getResourceType()).isEqualTo("latest_idr_rates");
    }

    @Test
    @DisplayName("fetch() returns FinanceDataResponse with resourceType and data")
    void fetch_returnsCorrectStructure() {
        Map<String, Object> mockResponse = Map.of(
                "base", "IDR",
                "date", "2024-01-02",
                "rates", Map.of("USD", 0.000064)
        );
        mockWebClient(mockResponse);

        StepVerifier.create(fetcher.fetch())
                .assertNext(response -> {
                    assertThat(response.getResourceType()).isEqualTo("latest_idr_rates");
                    assertThat(response.getData()).hasSize(1);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("fetch() injects USD_BuySpread_IDR field into response")
    @SuppressWarnings("unchecked")
    void fetch_injectsUsdBuySpreadIdr() {
        Map<String, Object> mockResponse = Map.of(
                "base", "IDR",
                "date", "2024-01-02",
                "rates", Map.of("USD", 0.000064)
        );
        mockWebClient(mockResponse);

        StepVerifier.create(fetcher.fetch())
                .assertNext(response -> {
                    Map<String, Object> entry = (Map<String, Object>) response.getData().get(0);
                    assertThat(entry).containsKey("USD_BuySpread_IDR");
                    assertThat(entry).containsKey("spreadFactor");
                    assertThat(entry.get("spreadFactor")).isEqualTo(0.00637);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("USD_BuySpread_IDR formula: (1 / Rate_USD) * (1 + spreadFactor)")
    @SuppressWarnings("unchecked")
    void fetch_calculatesUsdBuySpreadCorrectly() {
        double rateUsd = 0.000064;
        Map<String, Object> mockResponse = Map.of(
                "base", "IDR",
                "date", "2024-01-02",
                "rates", Map.of("USD", rateUsd)
        );
        mockWebClient(mockResponse);

        // Expected: (1 / 0.000064) * (1 + 0.00637) = 15724.375
        BigDecimal expected = new BigDecimal("1")
                .divide(new BigDecimal("0.000064"), 15, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("1.00637"))
                .setScale(8, java.math.RoundingMode.HALF_UP);

        StepVerifier.create(fetcher.fetch())
                .assertNext(response -> {
                    Map<String, Object> entry = (Map<String, Object>) response.getData().get(0);
                    BigDecimal actual = (BigDecimal) entry.get("USD_BuySpread_IDR");
                    assertThat(actual).isEqualByComparingTo(expected);
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
                .thenReturn(Mono.error(new RuntimeException("Network error")));

        StepVerifier.create(fetcher.fetch())
                .expectErrorMessage("Network error")
                .verify();
    }
}
