package strategy;
import com.allobank.idr_rate_aggregator.strategy.CurrenciesStrategy;
import com.allobank.idr_rate_aggregator.wrapper.CurrencyWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrenciesStrategyTest {
    @Mock private WebClient webClient;
    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CurrenciesStrategy strategy;

    @BeforeEach
    void setUp() {
        lenient().when(webClient.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void refreshData_ShouldParseCurrencyMap_AndStoreList() {
        Map<String, String> mockResponse = new HashMap<>();
        mockResponse.put("USD", "United States Dollar");
        mockResponse.put("IDR", "Indonesian Rupiah");

        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mockResponse));

        strategy.refreshData();

        List<CurrencyWrapper> result = strategy.fetchData();

        assertNotNull(result);
        assertEquals(2, result.size());

        boolean adaUSD = result.stream()
                .anyMatch(c -> c.getCode().equals("USD") && c.getName().equals("United States Dollar"));

        assertTrue(adaUSD, "Data USD harusnya ada di dalam list");
    }
}
