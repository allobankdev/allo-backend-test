package id.allobank.exchangerate.strategy;

import id.allobank.exchangerate.exception.ApiException;
import id.allobank.exchangerate.model.dto.LatestRatesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class LatestRatesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private LatestRatesStrategy strategy;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        strategy = new LatestRatesStrategy(webClient);

        var field = LatestRatesStrategy.class.getDeclaredField("username");
        field.setAccessible(true);
        field.set(strategy, "wahidinalambiyah");
    }

    @Test
    void fetch_success_shouldCalculateUsdBuySpreadIdr() {
        LatestRatesResponse mockResponse = new LatestRatesResponse();
        mockResponse.setBase("IDR");
        mockResponse.setDate("2024-01-01");
        mockResponse.setRates(Map.of("USD", 0.000065));

        mockWebClientChain();
        when(responseSpec.bodyToMono(LatestRatesResponse.class)).thenReturn(Mono.just(mockResponse));

        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) strategy.fetch();

        assertNotNull(result.get("fetchedAt"));
        assertEquals("latest_idr_rates", result.get("resourceType"));

        LatestRatesResponse data = (LatestRatesResponse) result.get("data");
        assertEquals(15488.615384615388, data.getUSD_BuySpread_IDR(), 1e-9);
    }

    @Test
    void fetch_whenUsdRateMissing_shouldThrowApiException() {
        LatestRatesResponse mockResponse = new LatestRatesResponse();
        mockResponse.setBase("IDR");
        mockResponse.setDate("2024-01-01");
        mockResponse.setRates(Map.of("EUR", 0.000059));

        mockWebClientChain();
        when(responseSpec.bodyToMono(LatestRatesResponse.class)).thenReturn(Mono.just(mockResponse));

        ApiException ex = assertThrows(ApiException.class, () -> strategy.fetch());

        assertEquals(HttpStatus.BAD_GATEWAY, ex.getStatus());
        assertTrue(ex.getMessage().contains("Field rates.USD is required"));
    }

    @Test
    void fetch_whenUsdRateNull_shouldThrowApiException() {
        LatestRatesResponse mockResponse = new LatestRatesResponse();
        mockResponse.setBase("IDR");
        mockResponse.setDate("2024-01-01");
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", null);
        mockResponse.setRates(rates);

        mockWebClientChain();
        when(responseSpec.bodyToMono(LatestRatesResponse.class)).thenReturn(Mono.just(mockResponse));

        ApiException ex = assertThrows(ApiException.class, () -> strategy.fetch());

        assertEquals(HttpStatus.BAD_GATEWAY, ex.getStatus());
        assertTrue(ex.getMessage().contains("Field rates.USD is required"));
    }

    @Test
    void fetch_whenHttpError_shouldPropagateApiException() {
        mockWebClientChain();
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.error(new ApiException("External API returned 4xx for endpoint /latest?base=IDR", HttpStatus.BAD_GATEWAY)));

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
