package id.allobank.exchangerate.strategy;

import id.allobank.exchangerate.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CurrencyStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private CurrencyStrategy strategy;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        strategy = new CurrencyStrategy(webClient);
    }

    @Test
    void fetch_success_shouldReturnCurrencies() {
        Map<String, String> currencies = Map.of(
                "USD", "United States Dollar",
                "IDR", "Indonesian Rupiah"
        );

        mockWebClientChain();
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(currencies));

        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) strategy.fetch();

        assertEquals("supported_currencies", result.get("resourceType"));
        assertNotNull(result.get("fetchedAt"));
        assertSame(currencies, result.get("data"));
    }

    @Test
    void fetch_whenHttpError_shouldPropagateApiException() {
        mockWebClientChain();
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.error(new ApiException(
                        "External API returned 4xx for endpoint /currencies",
                        HttpStatus.BAD_GATEWAY)));

        ApiException ex = assertThrows(ApiException.class, () -> strategy.fetch());

        assertEquals(HttpStatus.BAD_GATEWAY, ex.getStatus());
        assertTrue(ex.getMessage().contains("External API returned 4xx"));
    }

    private void mockWebClientChain() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    }
}
