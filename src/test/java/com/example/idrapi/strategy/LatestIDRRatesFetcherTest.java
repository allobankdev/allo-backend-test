package com.example.idrapi.strategy;

import com.example.idrapi.config.FrankfurterProperties;
import com.example.idrapi.dto.LatestRatesResponse;
import com.example.idrapi.strategy.impl.LatestIDRRatesFetcher;
import com.example.idrapi.util.CalculateUtil;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LatestIDRRatesFetcher Unit Tests")
class LatestIDRRatesFetcherTest {

    @Mock private WebClient webClient;
    @Mock private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private FrankfurterProperties properties;
    private LatestIDRRatesFetcher fetcher;
    private CalculateUtil calculateUtil;

    @BeforeEach
    void setUp() {
        properties = new FrankfurterProperties();
        properties.setBaseUrl("https://api.frankfurter.app");
        properties.setGithubUsername("mfathulkh");

        fetcher = new LatestIDRRatesFetcher(webClient, properties);
    }

    // ------------------------------------------------------------------ spread factor tests

    @Test
    @DisplayName("calculateSpreadFactor: correct computation for 'mfathulkh'")
    void spreadFactor_mfathulkh() {
        double factor = CalculateUtil.calculateSpreadFactor("mfathulkh");
        assertThat(factor).isEqualTo(0.00964, within(1e-10));
    }

    @Test
    @DisplayName("calculateSpreadFactor: uppercase username is lowercased before summing")
    void spreadFactor_uppercaseIsFolded() {
        double lower = CalculateUtil.calculateSpreadFactor("mfathulkh");
        double upper = CalculateUtil.calculateSpreadFactor("MFATHULKH");
        assertThat(lower).isEqualTo(upper);
    }

    @Test
    @DisplayName("calculateSpreadFactor: blank username throws IllegalArgumentException")
    void spreadFactor_blankUsernameThrows() {
        assertThatThrownBy(() -> CalculateUtil.calculateSpreadFactor("  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("blank");
    }

    @Test
    @DisplayName("calculateSpreadFactor: null username throws IllegalArgumentException")
    void spreadFactor_nullUsernameThrows() {
        assertThatThrownBy(() -> CalculateUtil.calculateSpreadFactor(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------------------------------------------ fetch() tests

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("fetch: returns record with USD_BuySpread_IDR calculated correctly")
    void fetch_returnsSpreadField() {
        // Arrange
        LatestRatesResponse mockResponse = new LatestRatesResponse();
        mockResponse.setBase("IDR");
        mockResponse.setDate("2024-01-05");
        mockResponse.setRates(Map.of("USD", 0.000064)); // 1 IDR = 0.000064 USD

        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri("/latest?base=IDR");
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(responseSpec).when(responseSpec).onStatus(any(), any());
        doReturn(Mono.just(mockResponse)).when(responseSpec).bodyToMono(LatestRatesResponse.class);

        // Act
        List<Map<String, Object>> results = fetcher.fetch();

        // Assert
        assertThat(results).hasSize(1);
        Map<String, Object> record = results.get(0);

        assertThat(record).containsKey("USD_BuySpread_IDR");
        assertThat(record).containsKey("spreadFactor");
        assertThat(record.get("base")).isEqualTo("IDR");
        assertThat(record.get("date")).isEqualTo("2024-01-05");

        double spreadFactor = (double) record.get("spreadFactor");
        assertThat(spreadFactor).isEqualTo(0.00964, within(1e-10));

        double expectedSpread = (1.0 / 0.000064) * (1.0 + 0.00964);
        double actualSpread = (double) record.get("USD_BuySpread_IDR");
        assertThat(actualSpread).isCloseTo(expectedSpread, within(0.01));
    }

    @Test
    @DisplayName("fetch: throws when USD rate is missing")
    @SuppressWarnings("unchecked")
    void fetch_throwsWhenUsdRateMissing() {
        LatestRatesResponse mockResponse = new LatestRatesResponse();
        mockResponse.setBase("IDR");
        mockResponse.setDate("2024-01-05");
        mockResponse.setRates(Map.of("EUR", 0.000059)); // No USD

        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri("/latest?base=IDR");
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(responseSpec).when(responseSpec).onStatus(any(), any());
        doReturn(Mono.just(mockResponse)).when(responseSpec).bodyToMono(LatestRatesResponse.class);

        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("USD rate not present");
    }

    @Test
    @DisplayName("getResourceType: returns correct key")
    void getResourceType_returnsCorrectKey() {
        assertThat(fetcher.getResourceType()).isEqualTo("latest_idr_rates");
    }
}
