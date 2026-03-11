package com.allo.idraggregator.application.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.allo.idraggregator.application.service.SpreadService;
import com.allo.idraggregator.domain.model.LatestRates;
import com.allo.idraggregator.infrastructure.client.FrankfurterClient;

class LatestIDRRateFetcherTest {

    @Mock
    private FrankfurterClient client;

    @Mock
    private SpreadService spread;

    private LatestIDRRateFetcher fetcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fetcher = new LatestIDRRateFetcher(client, spread);
    }

    @Test
    void shouldFetchLatestRatesAndApplySpread() {

        Map<String, Double> rates = Map.of("USD", 16000.0);
        LatestRates response = LatestRates.builder()
                .rates(rates)
                .build();

        when(client.getLatestRates("IDR")).thenReturn(response);
        when(spread.getUsdBuySpread(16000.0)).thenReturn(0.000065);

        LatestRates result = fetcher.fetchData();

        assertEquals(0.000065, result.getUsdBuySpreadIdr());
        assertEquals(rates, result.getRates());
    }
}