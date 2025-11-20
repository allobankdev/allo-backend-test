package com.nurmaya.allobank.idr_rate_aggregator.strategy;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import com.nurmaya.allobank.idr_rate_aggregator.client.FrankfurterClient;
import com.nurmaya.allobank.idr_rate_aggregator.dto.CurrencyListResponse;

import org.junit.jupiter.api.Test;

public class SupportedCurrenciesFetcherTest {

    @Test
    void testFetchData_ShouldReturnCurrencyList() {
        FrankfurterClient client = mock(FrankfurterClient.class);

        CurrencyListResponse mockResponse = new CurrencyListResponse();
        mockResponse.setCurrencies(Map.of(
                "USD", "United States Dollar",
                "IDR", "Indonesian Rupiah"
        ));

        when(client.getSupportedCurrencies()).thenReturn(mockResponse);

        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(client);

        List<CurrencyListResponse> result = fetcher.fetchData();

        assertEquals(1, result.size());
        assertEquals("United States Dollar", result.get(0).getCurrencies().get("USD"));
    }
}
