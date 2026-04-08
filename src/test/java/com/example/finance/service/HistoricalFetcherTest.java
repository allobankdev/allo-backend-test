package com.example.finance.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.finance.config.FrankfurterProperties;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalFetcherTest {

    @Mock
    private HitExternalApiService apiService;

    @InjectMocks
    private HistoricalFetcher fetcher;

    @Mock
	private FrankfurterProperties.Endpoints endpoints;
    
    @Test
    void shouldReturnHistoricalData() {
    	
    	when(apiService.endpoints()).thenReturn(endpoints);
    	when(endpoints.getHistorical()).thenReturn("/2024-01-01..2024-01-05");

    	Map<String, Object> rates = new HashMap<>();
    	Map<String, Object> usd = new HashMap<>();
        Map<String, Object> mock = new HashMap<>();
    	usd.put("USD", 0.000064);
    	rates.put("2024-01-01", usd);
        mock.put("rates", rates);

        when(apiService.get(any(), any(), eq(Map.class)))
                .thenReturn(mock);

        Object result = fetcher.fetchData();

        assertNotNull(result);
    }
}