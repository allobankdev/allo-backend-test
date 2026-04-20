package id.allobank.exchangerate.strategy;

import id.allobank.exchangerate.model.dto.HistoricalResponse;
import id.allobank.exchangerate.model.dto.LatestRatesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        // 🔥 inject field @Value secara manual
        var field = LatestRatesStrategy.class.getDeclaredField("username");
        field.setAccessible(true);
        field.set(strategy, "wahidinalambiyah"); // isi username kamu
    }

    @Test
    void testFetch_success() {

        LatestRatesResponse mockResponse = new LatestRatesResponse();
        mockResponse.setRates(Map.of("USD", 0.000065));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri(anyString()))
                .thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);

        // 🔥 WAJIB: mock onStatus
        when(responseSpec.onStatus(any(), any()))
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        Object result = strategy.fetch();

        assertNotNull(result);
    }

    @Test
    void testFetch_nullResponse_shouldThrow() {

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.empty());

        assertThrows(RuntimeException.class, () -> strategy.fetch());
    }

}
