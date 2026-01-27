package com.hend.backend.strategy.impl;

import com.hend.backend.dto.FrankfurterResponse;
import com.hend.backend.dto.LatestIdrRatesResult;
import com.hend.backend.util.SpreadCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author : hend wunga
 */

class LatestIdrRatesStrategyTest {

    private LatestIdrRatesStrategy strategy;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private final SpreadCalculator spreadCalculator = new SpreadCalculator();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        strategy = new LatestIdrRatesStrategy(webClient, spreadCalculator);
        // Pastikan username konsisten
        ReflectionTestUtils.setField(strategy, "githubUsername", "hendwunga");
    }


    @Test
    void testFetchData_Success() {
        FrankfurterResponse mockApiResponse = new FrankfurterResponse();
        mockApiResponse.setRates(Map.of("USD", 0.000064));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterResponse.class))
                .thenReturn(Mono.just(mockApiResponse));
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec); // Mock onStatus agar tidak null

        when(responseSpec.bodyToMono(FrankfurterResponse.class))
                .thenReturn(Mono.just(mockApiResponse));

        Object result = strategy.fetchData();

        assertInstanceOf(LatestIdrRatesResult.class, result);

        LatestIdrRatesResult dto = (LatestIdrRatesResult) result;
        assertEquals(0.00097, dto.getSpreadFactor(), 0.0000001);

    }

}
