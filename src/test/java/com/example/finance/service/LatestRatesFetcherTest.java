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
class LatestRatesFetcherTest {

	@Mock
	private HitExternalApiService apiService;

	@InjectMocks
	private LatestRatesFetcher fetcher;

	@Mock
	private FrankfurterProperties.Endpoints endpoints;

	@Test
	void shouldCalculateSpreadCorrectly() {

		// mock chain
		when(apiService.endpoints()).thenReturn(endpoints);
		when(endpoints.getLatest()).thenReturn("/latest");

		// mock response API
		Map<String, Object> mockResponse = new HashMap<>();
		Map<String, Double> rates = new HashMap<>();
		rates.put("USD", 0.000064);

		mockResponse.put("rates", rates);

		when(apiService.get(any(), any(), eq(Map.class))).thenReturn(mockResponse);

		// execute
		Object result = fetcher.fetchData();

		Map<String, Object> res = (Map<String, Object>) result;

		// verify
		assertNotNull(res.get("USD_BuySpread_IDR"));
	}
}