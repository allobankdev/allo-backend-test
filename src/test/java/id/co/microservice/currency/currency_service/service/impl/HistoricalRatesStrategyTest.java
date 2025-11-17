package id.co.microservice.currency.currency_service.service.impl;

import id.co.microservice.currency.currency_service.config.ExternalApiConfig;
import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.dto.FrankfurterResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoricalRatesStrategyTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ExternalApiConfig externalApiConfig;

    @InjectMocks
    private HistoricalRatesStrategy historicalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute_ReturnsCurrencyResponseDto() {
        String baseUrl = "http://mock-api.com";
        String expectedUrl = baseUrl + "/2024-01-01..2024-01-05?from=IDR&to=USD";

        FrankfurterResponseDto mockResponse = new FrankfurterResponseDto();
        mockResponse.setBase("IDR");
        mockResponse.setDate("2024-01-05");
        mockResponse.setStartDate("2024-01-01");
        mockResponse.setEndDate("2024-01-05");

        HashMap<String, Double> rates = new HashMap<>();
        rates.put("USD", 0.000065); // example conversion rate
        mockResponse.setRates(rates);

        when(externalApiConfig.getBaseUrl()).thenReturn(baseUrl);
        when(restTemplate.getForObject(expectedUrl, FrankfurterResponseDto.class)).thenReturn(mockResponse);

        CurrencyResponseDto result = historicalService.execute();

        assertEquals("IDR", result.getBase());
        assertEquals("2024-01-05", result.getDate());
        assertEquals("2024-01-01", result.getStartDate());
        assertEquals("2024-01-05", result.getEndDate());
        assertEquals(rates, result.getRates());

        verify(restTemplate, times(1)).getForObject(expectedUrl, FrankfurterResponseDto.class);
        verify(externalApiConfig, times(1)).getBaseUrl();
    }

}