package strategy;

import com.allobank.idr_rate_aggregator.strategy.LatestRateStrategy;
import com.allobank.idr_rate_aggregator.wrapper.ChangeRateWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LatestRateStrategyTest {
    @Mock
    private WebClient webClient;

    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private LatestRateStrategy strategy;

    @BeforeEach
    void setUp() {
        lenient().when(webClient.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void refreshData_Success() {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("date", "2024-01-30");

        Map<String, Object> rates = new HashMap<>();
        rates.put("USD", 0.000064);
        mockResponse.put("rates", rates);

        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mockResponse));

        strategy.refreshData();

        List<ChangeRateWrapper> result = strategy.fetchData();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("IDR", result.get(0).getBase());
        assertTrue(result.get(0).getBuySpread().compareTo(BigDecimal.ZERO) > 0);
    }
}
