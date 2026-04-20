package id.allobank.exchangerate.strategy;

import id.allobank.exchangerate.exception.ApiException;
import id.allobank.exchangerate.model.dto.HistoricalResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class HistoricalStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private HistoricalStrategy strategy;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        strategy = new HistoricalStrategy(webClient);
    }

    @Test
    void fetch_success_shouldReturnHistoricalData() {
        HistoricalResponse mockResponse = new HistoricalResponse();
        mockResponse.setBase("IDR");
        mockResponse.setRates(Map.of(
                "2024-01-01", Map.of("USD", 0.000064),
                "2024-01-02", Map.of("USD", 0.000065)
        ));

        mockWebClientChain();
        when(responseSpec.bodyToMono(HistoricalResponse.class)).thenReturn(Mono.just(mockResponse));

        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) strategy.fetch();

        assertEquals("historical_idr_usd", result.get("resourceType"));
        assertNotNull(result.get("fetchedAt"));
        assertSame(mockResponse, result.get("data"));
    }

    @Test
    void fetch_whenHttpError_shouldPropagateApiException() {
        mockWebClientChain();
        when(responseSpec.bodyToMono(HistoricalResponse.class))
                .thenReturn(Mono.error(new ApiException(
                        "External API returned 5xx for endpoint /2024-01-01..2024-01-05?from=IDR&to=USD",
                        HttpStatus.BAD_GATEWAY)));

        ApiException ex = assertThrows(ApiException.class, () -> strategy.fetch());

        assertEquals(HttpStatus.BAD_GATEWAY, ex.getStatus());
        assertTrue(ex.getMessage().contains("External API returned 5xx"));
    }

    private void mockWebClientChain() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    }
}
