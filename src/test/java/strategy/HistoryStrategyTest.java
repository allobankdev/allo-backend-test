package strategy;
import com.allobank.idr_rate_aggregator.strategy.HistoryStrategy;
import com.allobank.idr_rate_aggregator.wrapper.HistoryRateWrapper;
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
public class HistoryStrategyTest {
    @Mock private WebClient webClient;
    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private HistoryStrategy strategy;

    @BeforeEach
    void setUp() {
        lenient().when(webClient.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void refreshData_ShouldParseNestedMap_AndStoreHistoryCorrectly() {
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Object> innerRates = new HashMap<>();
        innerRates.put("USD", 0.000065);
        Map<String, Object> ratesByDate = new HashMap<>();
        ratesByDate.put("2024-01-01", innerRates);

        mockResponse.put("rates", ratesByDate);

        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mockResponse));

        strategy.refreshData();

        List<?> result = strategy.fetchData();

        assertNotNull(result);
        assertEquals(1, result.size());

        HistoryRateWrapper item = (HistoryRateWrapper) result.get(0);
        assertEquals("2024-01-01", item.getDate().toString());
        assertEquals(0, item.getRateUsd().compareTo(new BigDecimal("0.000065")));
    }
}
