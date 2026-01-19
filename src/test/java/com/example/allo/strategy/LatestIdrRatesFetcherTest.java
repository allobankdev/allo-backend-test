package com.example.allo.strategy;

import com.example.allo.client.FrankfurterClient;
import com.example.allo.dto.LatestRatesResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesFetcherTest {

    @Mock
    FrankfurterClient client;

    @Test
    void shouldCalculateSpread() {
        LatestRatesResponse res = new LatestRatesResponse();
        res.setRates(Map.of("USD", 0.000065));

        when(client.getLatestRates("IDR")).thenReturn(res);

        LatestIdrRatesFetcher fetcher =
                new LatestIdrRatesFetcher(client, "ilhamharazky");

        LatestRatesResponse out =
                (LatestRatesResponse) fetcher.fetch();

        assertNotNull(out.getUsdBuySpreadIdr());
    }
}
