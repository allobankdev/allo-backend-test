package com.api.allorestapi.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SupportedCurrenciesFetcher Unit Tests")
class SupportedCurrenciesFetcherTest {

    @Mock private WebClient webClient;
    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new SupportedCurrenciesFetcher(webClient);
    }

    @SuppressWarnings("unchecked")
    private void mockWebClient(Map<String, String> responseBody) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just((Map) responseBody));
    }

    @Test
    @DisplayName("getResourceType() returns 'supported_currencies'")
    void getResourceType_returnsCorrectKey() {
        assertThat(fetcher.getResourceType()).isEqualTo("supported_currencies");
    }

    @Test
    @DisplayName("fetch() maps each currency into a {code, name} object")
    @SuppressWarnings("unchecked")
    void fetch_mapsCurrencyToCodeNamePairs() {
        mockWebClient(Map.of(
                "USD", "US Dollar",
                "IDR", "Indonesian Rupiah",
                "EUR", "Euro"
        ));

        StepVerifier.create(fetcher.fetch())
                .assertNext(response -> {
                    assertThat(response.getResourceType()).isEqualTo("supported_currencies");
                    assertThat(response.getData()).hasSize(3);

                    response.getData().forEach(item -> {
                        Map<String, String> entry = (Map<String, String>) item;
                        assertThat(entry).containsKeys("code", "name");
                    });
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("fetch() sorts currencies by code alphabetically")
    @SuppressWarnings("unchecked")
    void fetch_sortsByCodeAlphabetically() {
        mockWebClient(Map.of(
                "USD", "US Dollar",
                "AUD", "Australian Dollar",
                "IDR", "Indonesian Rupiah"
        ));

        StepVerifier.create(fetcher.fetch())
                .assertNext(response -> {
                    List<Object> data = response.getData();
                    String first = (String) ((Map<String, String>) data.get(0)).get("code");
                    String last  = (String) ((Map<String, String>) data.get(2)).get("code");
                    assertThat(first).isEqualTo("AUD");
                    assertThat(last).isEqualTo("USD");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("fetch() propagates WebClient errors")
    void fetch_propagatesError() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.error(new RuntimeException("Service unavailable")));

        StepVerifier.create(fetcher.fetch())
                .expectErrorMessage("Service unavailable")
                .verify();
    }
}
