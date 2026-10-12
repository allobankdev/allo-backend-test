package com.allobank.strategy;

import com.allobank.dto.CurrenciesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesStrategyTest {
    
    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private ObjectMapper objectMapper;
    
    @InjectMocks
    private SupportedCurrenciesStrategy strategy;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(strategy, "baseUrl", "https://api.frankfurter.app");
    }
    
    @Test
    void testFetchDataReturnsCurrenciesResponse() throws Exception {
        // Arrange
        Map<String, String> currencies = new HashMap<>();
        currencies.put("USD", "United States Dollar");
        currencies.put("EUR", "Euro");
        currencies.put("IDR", "Indonesian Rupiah");
        
        Map<String, String> mockResponse = currencies;
        
        String jsonResponse = "{\"USD\":\"United States Dollar\",\"EUR\":\"Euro\",\"IDR\":\"Indonesian Rupiah\"}";
        
        when(restTemplate.getForObject(
                anyString(),
                eq(String.class)
        )).thenReturn(jsonResponse);
        
        when(objectMapper.readValue(jsonResponse, Map.class))
                .thenReturn(mockResponse);
        
        // Act
        Object result = strategy.fetchData();
        
        // Assert
        assertNotNull(result);
        assertInstanceOf(CurrenciesResponse.class, result);
        
        CurrenciesResponse response = (CurrenciesResponse) result;
        assertNotNull(response.getCurrencies());
        assertEquals(3, response.getCurrencies().size());
        assertTrue(response.getCurrencies().containsKey("USD"));
    }
    
    @Test
    void testGetResourceType() {
        assertEquals("supported_currencies", strategy.getResourceType());
    }
    
    @Test
    void testFetchDataThrowsExceptionOnNullResponse() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(null);
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> strategy.fetchData());
    }
}
