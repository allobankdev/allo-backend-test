package com.allobank.finance.fetcher;

import com.allobank.finance.dto.FinanceDataResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesFetcherTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private SupportedCurrenciesFetcher fetcher;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        fetcher = new SupportedCurrenciesFetcher(webClient, objectMapper);
    }

    @Test
    void fetch_shouldReturnCurrenciesMap() throws Exception {
        // Arrange
        String mockJson = """
                {
                  "AUD": "Australian Dollar",
                  "IDR": "Indonesian Rupiah",
                  "USD": "US Dollar",
                  "EUR": "Euro"
                }
                """;

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(mockJson));

        // Act
        FinanceDataResponse result = fetcher.fetch();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getResourceType()).isEqualTo("supported_currencies");
        assertThat(result.getUsdBuySpreadIdr()).isNull(); // no spread for currencies
        assertThat(result.getFetchedAt()).isNotNull();
        assertThat(result.getData()).isInstanceOf(Map.class);

        @SuppressWarnings("unchecked")
        Map<String, String> currencies = (Map<String, String>) result.getData();
        assertThat(currencies).hasSize(4);
        assertThat(currencies).containsEntry("IDR", "Indonesian Rupiah");
        assertThat(currencies).containsEntry("USD", "US Dollar");
    }

    @Test
    void fetch_shouldThrowWhenApiReturnsBlank() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(""));

        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to fetch supported currencies");
    }

    @Test
    void getResourceType_shouldReturnCorrectKey() {
        assertThat(fetcher.getResourceType()).isEqualTo("supported_currencies");
    }
}
