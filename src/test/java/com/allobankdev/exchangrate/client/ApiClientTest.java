package com.allobankdev.exchangrate.client;

import com.allobankdev.exchangrate.dto.CurrencyResponse;
import com.allobankdev.exchangrate.dto.HistoricalResponse;
import com.allobankdev.exchangrate.dto.LatestRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ApiClient apiClient;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(apiClient, "baseUrl", "https://api.frankfurter.app");
    }

    @Test
    void getHistoricalRates() {
        HistoricalResponse mockResponse = new HistoricalResponse();
        mockResponse.setBase("IDR");

        when(restTemplate.getForObject(contains("from=IDR&to=USD"), eq(HistoricalResponse.class)))
                .thenReturn(mockResponse);

        HistoricalResponse result = apiClient.getHistoricalRates();
        assertNotNull(result);
        assertEquals("IDR", result.getBase());
    }

    @Test
    void getCurrencies() {
        CurrencyResponse mockResponse = new CurrencyResponse();
        mockResponse.put("USD", "United States Dollar");

        when(restTemplate.getForObject(contains("currencies"), eq(CurrencyResponse.class)))
                .thenReturn(mockResponse);

        CurrencyResponse result = apiClient.getCurrencies();
        assertNotNull(result);
        assertTrue(result.containsKey("USD"));
    }

    @Test
    void getLatestRates() {
        LatestRateResponse mockResponse = new LatestRateResponse();
        mockResponse.setBase("IDR");

        when(restTemplate.getForObject(anyString(), eq(LatestRateResponse.class)))
                .thenReturn(mockResponse);

        LatestRateResponse result = apiClient.getLatestRates();
        assertNotNull(result);
        assertEquals("IDR", result.getBase());
    }
}