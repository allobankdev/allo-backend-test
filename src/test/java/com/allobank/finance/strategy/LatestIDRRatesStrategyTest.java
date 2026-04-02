package com.allobank.finance.strategy;

import com.allobank.finance.model.FrankfurterResponse;
import com.allobank.finance.repository.FinanceDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIDRRatesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private FinanceDataRepository financeDataRepository;

    @InjectMocks
    private LatestIDRRatesStrategy strategy;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Captor
    private ArgumentCaptor<List<Map<String, Object>>> dataCaptor;

    @BeforeEach
    void setUp() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    }

    @Test
    void fetchAndCacheData_Success() {
        FrankfurterResponse mockResponse = new FrankfurterResponse();
        mockResponse.setBase("IDR");
        mockResponse.setDate("2024-03-08");
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 0.000065);
        mockResponse.setRates(rates);

        when(responseSpec.bodyToMono(FrankfurterResponse.class)).thenReturn(Mono.just(mockResponse));

        strategy.fetchAndCacheData();

        verify(financeDataRepository).saveData(eq("latest_idr_rates"), dataCaptor.capture());
        List<Map<String, Object>> capturedData = dataCaptor.getValue();
        when(financeDataRepository.findDataByResourceType("latest_idr_rates")).thenReturn(Optional.of(capturedData));

        List<Map<String, Object>> result = strategy.getData().orElseThrow();
        assertEquals(1, result.size());
        assertEquals("latest_idr_rates", result.get(0).get("resourceType"));
        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) result.get(0).get("data");
        assertEquals("IDR", payload.get("base"));
        assertEquals("2024-03-08", payload.get("date"));

        assertTrue(payload.containsKey("USD_BuySpread_IDR"));
        double actual = (double) payload.get("USD_BuySpread_IDR");
        double expectedRate = (1.0 / 0.000065);
        double expectedSpread = 0.01209;
        double expected = expectedRate * (1 + expectedSpread);

        assertEquals(expected, actual, 0.001);
    }

    @Test
    void fetchAndCacheData_Error() {
        when(responseSpec.bodyToMono(FrankfurterResponse.class)).thenReturn(Mono.error(new RuntimeException("API Error")));

        strategy.fetchAndCacheData();

        verify(financeDataRepository, never()).saveData(anyString(), any());
        when(financeDataRepository.findDataByResourceType(anyString())).thenReturn(Optional.empty());
        assertTrue(strategy.getData().isEmpty());
    }
}
