package id.allobank.exchangerate.strategy;

import id.allobank.exchangerate.model.dto.HistoricalResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class HistoricalStrategyTest {

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
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        strategy = new HistoricalStrategy(webClient);
    }

    @Test
    void testHistoricalFetch() {

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        // 🔥 INI YANG KURANG
        when(responseSpec.onStatus(any(), any()))
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(HistoricalResponse.class))
                .thenReturn(Mono.just(new HistoricalResponse()));

        Object result = strategy.fetch();

        assertNotNull(result);
    }

}
