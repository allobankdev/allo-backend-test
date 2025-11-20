package com.allo.idr;

import com.allo.idr.client.ExternalApiClient;
import com.allo.idr.exception.ExternalApiException;
import com.allo.idr.model.CurrencyResponse;
import com.allo.idr.service.SupportedCurrenciesFetcher;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SupportCurrenciesFetcherTest {
    @Test
    void testFetchCurrenciesSuccess(){
        ExternalApiClient expMock = Mockito.mock(ExternalApiClient.class);
        Mockito.when(expMock.getCurrencies()).thenReturn(Map.of("USD","United States Dollar","IDR","Indonesian Rupiah"));
        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(expMock);
        List<CurrencyResponse> list = fetcher.fetcData();

        assertNotNull(list);
        assertTrue(list.stream().anyMatch(curr -> "IDR".equals(curr.getCode())));
    }

    @Test
    void testFetchCurrenciesError(){
        ExternalApiClient expMock = Mockito.mock(ExternalApiClient.class);
        Mockito.when(expMock.getCurrencies()).thenThrow(new ExternalApiException("down"));

        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(expMock);
        assertThrows(ExternalApiException.class, fetcher::fetcData);
    }
}
