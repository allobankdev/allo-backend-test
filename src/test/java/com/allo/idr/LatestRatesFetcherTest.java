package com.allo.idr;

import com.allo.idr.client.ExternalApiClient;
import com.allo.idr.exception.ExternalApiException;
import com.allo.idr.model.LatestRateResponse;
import com.allo.idr.service.LatestRatesFetcher;
import com.allo.idr.util.SpreadCalculator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LatestRatesFetcherTest {
    @Test
    void testSpreadCalculationSuccess() {
        ExternalApiClient mockClient = Mockito.mock(ExternalApiClient.class);
        Mockito.when(mockClient.getLatestBaseIdr()).thenReturn(Map.of(
                "base","IDR",
                "date","2024-01-01",
                "rates", Map.of("USD", 0.000064)
        ));

        SpreadCalculator spreadCalculator = new SpreadCalculator("test");
        LatestRatesFetcher fetcher = new LatestRatesFetcher(mockClient, spreadCalculator);
        List<LatestRateResponse> res = fetcher.fetcData();

        assertNotNull(res);
        assertEquals(1, res.size());
        LatestRateResponse resDto = res.get(0);
        assertEquals("IDR", resDto.getBase());
        assertNotNull(resDto.getUsdBuySpreadIdr());
    }

    @Test
    void testSpreadCalculationError(){
        ExternalApiClient expClient = Mockito.mock(ExternalApiClient.class);
        Mockito.when(expClient.getLatestBaseIdr()).thenThrow(new ExternalApiException("down"));

        SpreadCalculator spreadCalculator = new SpreadCalculator("test");
        LatestRatesFetcher fetcher = new LatestRatesFetcher(expClient, spreadCalculator);
        assertThrows(ExternalApiException.class, fetcher::fetcData);
    }
}
