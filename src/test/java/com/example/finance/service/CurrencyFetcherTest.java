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
class CurrencyFetcherTest {

    @Mock
    private HitExternalApiService apiService;

    @InjectMocks
    private CurrencyFetcher fetcher;
    
    @Mock
	private FrankfurterProperties.Endpoints endpoints;

    @Test
    void shouldReturnCurrencies() {
    	
    	when(apiService.endpoints()).thenReturn(endpoints);
    	when(endpoints.getCurrencies()).thenReturn("/currencies");

        Map<String, String> mock = new HashMap<>();
        mock.put("USD", "United States Dollar");

        when(apiService.get(any(), any(), eq(Map.class)))
                .thenReturn(mock);

        Object result = fetcher.fetchData();

        assertNotNull(result);
        assertFalse(((Map<?, ?>) result).isEmpty());
    }
}