package com.example.idrapi.strategy;

import com.example.idrapi.strategy.impl.SupportedCurrenciesFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SupportedCurrenciesFetcher Unit Tests")
class SupportedCurrenciesFetcherTest {

    @Mock private WebClient webClient;
    @Mock private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new SupportedCurrenciesFetcher(webClient);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("fetch: transforms currency map to list of {code, name} records")
    void fetch_transformsMapToList() {
        // Arrange
        Map<String, String> mockCurrencies = Map.of(
                "USD", "US Dollar",
                "EUR", "Euro",
                "IDR", "Indonesian Rupiah"
        );

        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri("/currencies");
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(responseSpec).when(responseSpec).onStatus(any(), any());
        doReturn(Mono.just(mockCurrencies)).when(responseSpec)
                .bodyToMono(any(ParameterizedTypeReference.class));

        // Act
        List<Map<String, Object>> results = fetcher.fetch();

        // Assert
        assertThat(results).hasSize(3);
        results.forEach(record -> {
            assertThat(record).containsKeys("code", "name");
            assertThat(record.get("code")).isNotNull();
            assertThat(record.get("name")).isNotNull();
        });

        // Verify IDR is present
        boolean hasIDR = results.stream()
                .anyMatch(r -> "IDR".equals(r.get("code")) && "Indonesian Rupiah".equals(r.get("name")));
        assertThat(hasIDR).isTrue();
    }

    @Test
    @DisplayName("getResourceType: returns correct key")
    void getResourceType_returnsCorrectKey() {
        assertThat(fetcher.getResourceType()).isEqualTo("supported_currencies");
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("fetch: throws IllegalStateException when API returns null")
    void fetch_throwsOnNullResponse() {
        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri("/currencies");
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(responseSpec).when(responseSpec).onStatus(any(), any());
        doReturn(Mono.empty()).when(responseSpec)
                .bodyToMono(any(ParameterizedTypeReference.class));

        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("null response");
    }
}
