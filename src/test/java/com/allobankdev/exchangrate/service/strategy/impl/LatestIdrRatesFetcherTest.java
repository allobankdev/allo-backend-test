package com.allobankdev.exchangrate.service.strategy.impl;

import com.allobankdev.exchangrate.client.ApiClient;
import com.allobankdev.exchangrate.dto.LatestRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesFetcherTest {

    @Mock
    private ApiClient client;

    @InjectMocks
    private LatestIdrRatesFetcher fetcher;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fetcher, "githubUsername", "mikleo04");
    }

    @Test
    void testSpreadCalculation() {
        LatestRateResponse mockResponse = new LatestRateResponse();
        mockResponse.setRates(Map.of("USD", BigDecimal.valueOf(15000.0)));

        when(client.getLatestRates()).thenReturn(mockResponse);
        LatestRateResponse result = (LatestRateResponse) fetcher.fetch();

        assertNotNull(result);
        assertTrue(result.getUsdBuySpreadIdr().compareTo(BigDecimal.ZERO) >= 0);
        assertTrue(result.getUsdBuySpreadIdr().compareTo(BigDecimal.valueOf(0.01)) < 0);
    }
}