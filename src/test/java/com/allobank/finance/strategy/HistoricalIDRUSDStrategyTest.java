package com.allobank.finance.strategy;

import com.allobank.finance.model.FrankfurterHistoricalResponse;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricalIDRUSDStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private FinanceDataRepository financeDataRepository;

    @InjectMocks
    private HistoricalIDRUSDStrategy strategy;

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
        FrankfurterHistoricalResponse mockResponse = new FrankfurterHistoricalResponse();
        mockResponse.setBase("IDR");
        mockResponse.setStart_date("2024-01-01");
        mockResponse.setEnd_date("2024-01-05");
        Map<String, Map<String, Double>> rates = new HashMap<>();
        Map<String, Double> day1 = new HashMap<>();
        day1.put("USD", 0.000065);
        rates.put("2024-01-01", day1);
        mockResponse.setRates(rates);

        when(responseSpec.bodyToMono(FrankfurterHistoricalResponse.class)).thenReturn(Mono.just(mockResponse));

        strategy.fetchAndCacheData();

        verify(financeDataRepository).saveData(eq("historical_idr_usd"), dataCaptor.capture());
        List<Map<String, Object>> capturedData = dataCaptor.getValue();
        when(financeDataRepository.findDataByResourceType("historical_idr_usd")).thenReturn(Optional.of(capturedData));

        List<Map<String, Object>> result = strategy.getData().orElseThrow();
        assertEquals(1, result.size());
        assertEquals("historical_idr_usd", result.get(0).get("resourceType"));
        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) result.get(0).get("data");
        assertEquals("IDR", payload.get("base"));
        assertEquals("2024-01-01", payload.get("start_date"));
        assertEquals("2024-01-05", payload.get("end_date"));
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Double>> ratesResult = (Map<String, Map<String, Double>>) payload.get("rates");
        assertEquals(0.000065, ratesResult.get("2024-01-01").get("USD"), 0.0000001);
    }
}
