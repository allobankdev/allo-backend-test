package com.musfiul.idrrateaggregator.service.impl;

import com.musfiul.idrrateaggregator.client.FrankfurterClient;
import com.musfiul.idrrateaggregator.dto.LatestRatesResponse;
import com.musfiul.idrrateaggregator.service.fetcher.impl.LatestIdrRatesFetcherImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesFetcherImplTest {

    @Mock
    private FrankfurterClient client;

    @InjectMocks
    private LatestIdrRatesFetcherImpl fetcher;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fetcher, "username", "musfiulchaggi");
    }

    @Test
    void shouldFetchDataAndCalculateSpread() {
        LatestRatesResponse mockResponse = new LatestRatesResponse();

        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", new BigDecimal("0.000059"));
        mockResponse.setRates(rates);

        when(client.getLatestRates()).thenReturn(mockResponse);

        LatestRatesResponse result = fetcher.fetchData();

        assertNotNull(result);
        assertNotNull(result.getUsdBuySpreadIdr());
        assertEquals(0, result.getUsdBuySpreadIdr().compareTo(new BigDecimal("17014.237288135611936")));
    }

    @Test
    void shouldReturnCorrectResourceType() {
        assertEquals("LATEST_IDR_RATES", fetcher.getType().name());
    }
}