package com.example.financedata.fetcher;

import com.example.financedata.dto.CurrenciesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupportedCurrenciesFetcherTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        fetcher = new SupportedCurrenciesFetcher(webClient);
    }

    @Test
    void testFetchCurrencies() {

        CurrenciesDto mockResponse = new CurrenciesDto();
        mockResponse.setCurrencies(
                Map.of(
                        "USD", "United States Dollar",
                        "JPY", "Japanese Yen",
                        "EUR", "Euro"
                )
        );

        // Mock WebClient chain
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CurrenciesDto.class)).thenReturn(Mono.just(mockResponse));

        // ACT: block the Mono!
        Map<String, Object> result = (Map<String, Object>) fetcher.fetch().block();

        assertNotNull(result);
        assertTrue(result.containsKey("currencies"));

        Map<?, ?> currencies = (Map<?, ?>) result.get("currencies");
        assertEquals(3, currencies.size());
    }
}
