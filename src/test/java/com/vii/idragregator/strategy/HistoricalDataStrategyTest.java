package com.vii.idragregator.strategy;

import com.vii.idragregator.dto.HistoricalRateDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricalDataStrategyTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private HistoricalDataStrategy historicalDataStrategy;

    @Test
    void testFetchHistoricalData_Success() {
        // Mocking Data
        HistoricalRateDTO mockDto = new HistoricalRateDTO();
        mockDto.setBase("IDR");
        mockDto.setStart_date("2024-01-01");
        mockDto.setEnd_date("2024-01-05");

        Map<String, Map<String, Double>> rates = new HashMap<>();
        rates.put("2024-01-01", Map.of("USD", 0.000064));
        mockDto.setRates(rates);

        // Mock WebClient chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HistoricalRateDTO.class)).thenReturn(Mono.just(mockDto));

        // Execute
        HistoricalRateDTO result = (HistoricalRateDTO) historicalDataStrategy.fetch();

        // Verify
        assertNotNull(result);
        assertEquals("IDR", result.getBase());
        assertTrue(result.getRates().containsKey("2024-01-01"));
    }
}