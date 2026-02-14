package com.vii.idragregator.strategy;

import com.vii.idragregator.dto.LatestRateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestRatesStrategyTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private LatestRatesStrategy latestRatesStrategy;

    @BeforeEach
    void setUp() {
        // Set manual value untuk @Value annotation
        ReflectionTestUtils.setField(latestRatesStrategy, "githubUsername", "luthfiaryaa");
    }

    @Test
    void testFetch_SuccessWithSpreadCalculation() {
        // Prepare Mock Data
        LatestRateDTO mockDto = new LatestRateDTO();
        mockDto.setBase("IDR");
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 0.000063); // Example rate
        mockDto.setRates(rates);

        // Mock WebClient Chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRateDTO.class)).thenReturn(Mono.just(mockDto));

        // Execute
        LatestRateDTO result = (LatestRateDTO) latestRatesStrategy.fetch();

        // Verify
        assertNotNull(result);
        assertNotNull(result.getUsd_buySpread_idr());

        assertTrue(result.getUsd_buySpread_idr() > 15000);
        System.out.println("Calculated Buy Spread: " + result.getUsd_buySpread_idr());
    }
}