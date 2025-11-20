package com.allo.idr;

import com.allo.idr.client.ExternalApiClient;
import com.allo.idr.exception.ExternalApiException;
import com.allo.idr.model.HistoricalRatesResponse;
import com.allo.idr.service.HistoricalRatesFetcher;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HistoricalRatesFetcherTest {
    @Test
    void testFetchHistoricalSuccess(){
        ExternalApiClient expMockClient = Mockito.mock(ExternalApiClient.class);
        Map<String, Object> resConvert = Map.of(
                "rates", Map.of(
                        "2024-01-01",
                        Map.of("USD", 0.000064),
                        "2024-01-02",
                        Map.of("USD", 0.000065)
                )
        );
        Mockito.when(expMockClient.getHistoricalIdrToUsd("2024-01-01", "2024-01-05"))
                .thenReturn(resConvert);
        HistoricalRatesFetcher fetcher = new HistoricalRatesFetcher(expMockClient);
        List<HistoricalRatesResponse> list = fetcher.fetcData();

        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    void testFetchHistoricalError(){
        ExternalApiClient expMockClient = Mockito.mock(ExternalApiClient.class);
        Mockito.when(expMockClient.getHistoricalIdrToUsd(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new ExternalApiException("down"));
        HistoricalRatesFetcher fetcher = new HistoricalRatesFetcher(expMockClient);
        assertThrows(ExternalApiException.class, fetcher::fetcData);
    }
}
