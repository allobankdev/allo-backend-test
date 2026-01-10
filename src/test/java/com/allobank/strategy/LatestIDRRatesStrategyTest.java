package com.allobank.strategy;

import com.allobank.dto.LatestRatesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIDRRatesStrategyTest {
    
    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private ObjectMapper objectMapper;
    
    @InjectMocks
    private LatestIDRRatesStrategy strategy;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(strategy, "baseUrl", "https://api.frankfurter.app");
        ReflectionTestUtils.setField(strategy, "githubUsername", "testuser");
    }
    
    @Test
    void testFetchDataReturnsLatestRatesResponse() throws Exception {
        // Arrange
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", new BigDecimal("0.000067"));
        rates.put("EUR", new BigDecimal("0.000061"));
        
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("base", "IDR");
        mockResponse.put("date", "2024-01-05");
        mockResponse.put("rates", rates);
        
        String jsonResponse = "{\"base\":\"IDR\",\"date\":\"2024-01-05\",\"rates\":{\"USD\":0.000067,\"EUR\":0.000061}}";
        
        when(restTemplate.getForObject(
                eq("https://api.frankfurter.app/latest?base=IDR"),
                eq(String.class)
        )).thenReturn(jsonResponse);
        
        when(objectMapper.readValue(jsonResponse, Map.class))
                .thenReturn(mockResponse);
        
        // Act
        Object result = strategy.fetchData();
        
        // Assert
        assertNotNull(result);
        assertInstanceOf(LatestRatesResponse.class, result);
        
        LatestRatesResponse response = (LatestRatesResponse) result;
        assertEquals("IDR", response.getBase());
        assertEquals("2024-01-05", response.getDate());
        assertNotNull(response.getUsdBuySpreadIDR());
        assertTrue(response.getUsdBuySpreadIDR().compareTo(BigDecimal.ZERO) > 0);
    }
    
    @Test
    void testSpreadFactorCalculation() throws Exception {
        // Test that USD_BuySpread_IDR is calculated and returned
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", new BigDecimal("0.000067"));
        
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("base", "IDR");
        mockResponse.put("date", "2024-01-05");
        mockResponse.put("rates", rates);
        
        String jsonResponse = "{\"base\":\"IDR\",\"date\":\"2024-01-05\",\"rates\":{\"USD\":0.000067}}";
        
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(jsonResponse);
        when(objectMapper.readValue(jsonResponse, Map.class))
                .thenReturn(mockResponse);
        
        // Act
        Object result = strategy.fetchData();
        
        // Assert
        LatestRatesResponse response = (LatestRatesResponse) result;
        assertNotNull(response.getUsdBuySpreadIDR());
        
        // The spread factor should result in a value greater than the inverse rate
        BigDecimal inverseRate = BigDecimal.ONE.divide(new BigDecimal("0.000067"), 10, java.math.RoundingMode.HALF_UP);
        assertTrue(response.getUsdBuySpreadIDR().compareTo(inverseRate) > 0);
    }
    
    @Test
    void testGetResourceType() {
        assertEquals("latest_idr_rates", strategy.getResourceType());
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
