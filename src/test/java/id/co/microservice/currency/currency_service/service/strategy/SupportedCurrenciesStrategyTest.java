package id.co.microservice.currency.currency_service.service.strategy;

import id.co.microservice.currency.currency_service.config.ExternalApiConfig;
import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class SupportedCurrenciesStrategyTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ExternalApiConfig externalApiConfig;

    @InjectMocks
    private SupportedCurrenciesStrategy currenciesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute_ReturnsCurrencyResponseDto() {
        String baseUrl = "http://mock-api.com";
        String expectedUrl = baseUrl + "/currencies";

        HashMap<String, String> mockCurrencies = new HashMap<>();
        mockCurrencies.put("USD", "United States Dollar");
        mockCurrencies.put("EUR", "Euro");

        when(externalApiConfig.getBaseUrl()).thenReturn(baseUrl);
        when(restTemplate.getForObject(expectedUrl, HashMap.class)).thenReturn(mockCurrencies);

        CurrencyResponseDto result = currenciesService.execute();

        assertEquals(mockCurrencies, result.getCurrencies());
        verify(restTemplate, times(1)).getForObject(expectedUrl, HashMap.class);
        verify(externalApiConfig, times(1)).getBaseUrl();
    }
}