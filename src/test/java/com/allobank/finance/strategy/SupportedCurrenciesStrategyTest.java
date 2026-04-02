package com.allobank.finance.strategy;

import com.allobank.finance.repository.FinanceDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
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
class SupportedCurrenciesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private FinanceDataRepository financeDataRepository;

    @InjectMocks
    private SupportedCurrenciesStrategy strategy;

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
        Map<String, String> mockResponse = new HashMap<>();
        mockResponse.put("USD", "United States Dollar");
        mockResponse.put("IDR", "Indonesian Rupiah");

        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockResponse));

        strategy.fetchAndCacheData();

        verify(financeDataRepository).saveData(eq("supported_currencies"), dataCaptor.capture());
        List<Map<String, Object>> capturedData = dataCaptor.getValue();
        when(financeDataRepository.findDataByResourceType("supported_currencies")).thenReturn(Optional.of(capturedData));

        List<Map<String, Object>> result = strategy.getData().orElseThrow();
        assertEquals(1, result.size());
        assertEquals("supported_currencies", result.get(0).get("resourceType"));
        @SuppressWarnings("unchecked")
        Map<String, String> payload = (Map<String, String>) result.get(0).get("data");
        assertEquals("United States Dollar", payload.get("USD"));
        assertEquals("Indonesian Rupiah", payload.get("IDR"));
    }
}
