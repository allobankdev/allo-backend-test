package com.allobank.test.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"unchecked", "rawtypes"})
class LatestIdrRatesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private LatestIdrRatesStrategy strategy;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(strategy, "githubUsername", "tech-enthusiast-168");
    }

    @Test
    void testResourceTypeIsCorrect() {
        assertEquals("latest_idr_rates", strategy.getResourceType());
    }

    @Test
    void testFetchAndTransformAppliesSpreadFactor() {
        Map<String, Object> mockRates = new HashMap<>();
        mockRates.put("USD", 0.000064);

        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("rates", mockRates);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(mockResponse));

        Map<String, Object> result = (Map<String, Object>) strategy.fetchAndTransform();
        
        assertNotNull(result);
        assertTrue(result.containsKey("rates"));
        
        Map<String, Object> ratesMap = (Map<String, Object>) result.get("rates");
        assertTrue(ratesMap.containsKey("USD_BuySpread_IDR"));

        // USD_BuySpread_IDR = (1 / 0.000064) * (1 + 0.00865)
        double expectedSpreadValue = (1 / 0.000064) * 1.00865;
        double actualSpreadValue = (Double) ratesMap.get("USD_BuySpread_IDR");

        assertEquals(expectedSpreadValue, actualSpreadValue, 0.001);
    }
}
