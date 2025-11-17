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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LatestCurrencyServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ExternalApiConfig externalApiConfig;

    @InjectMocks
    private LatestCurrencyServiceImpl latestCurrencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute_ReturnsCurrencyResponseDto() {
        String baseUrl = "http://mock-api.com";
        String expectedUrl = baseUrl + "/latest?base=IDR";

        FrankfurterResponseDto mockResponse = new FrankfurterResponseDto();
        mockResponse.setBase("IDR");
        mockResponse.setDate("2024-01-05");

        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 0.000065); // example conversion rate
        mockResponse.setRates(rates);

        when(externalApiConfig.getBaseUrl()).thenReturn(baseUrl);
        when(restTemplate.getForObject(expectedUrl, FrankfurterResponseDto.class)).thenReturn(mockResponse);

        CurrencyResponseDto result = latestCurrencyService.execute();

        assertEquals("IDR", result.getBase());
        assertEquals("2024-01-05", result.getDate());
        assertEquals(rates, result.getRates());

        int asciiUserName = 0;
        for (char c : "AriAulia".toLowerCase().toCharArray()) {
            asciiUserName += c;
        }
        double spreadFactor = (asciiUserName % 1000) / 100000.0;
        double rateUSD = rates.get("USD");
        double expectedSpread = (1 / rateUSD) * (1 + spreadFactor);

        assertEquals(expectedSpread, result.getUsdBuySpreadIdr(), 0.000001);

        verify(restTemplate, times(1)).getForObject(expectedUrl, FrankfurterResponseDto.class);
        verify(externalApiConfig, times(1)).getBaseUrl();
    }

}