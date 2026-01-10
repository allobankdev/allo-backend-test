package com.allobank.strategy;

import com.allobank.dto.HistoricalRatesResponse;
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
class HistoricalIDRUSDStrategyTest {
    
    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private ObjectMapper objectMapper;
    
    @InjectMocks
    private HistoricalIDRUSDStrategy strategy;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(strategy, "baseUrl", "https://api.frankfurter.app");
    }
    
    @Test
    void testFetchDataReturnsHistoricalRatesResponse() throws Exception {
        // Arrange
        Map<String, BigDecimal> rates2024_01_01 = new HashMap<>();
        rates2024_01_01.put("USD", new BigDecimal("0.000067"));
        
        Map<String, Map<String, BigDecimal>> historicalRates = new HashMap<>();
        historicalRates.put("2024-01-01", rates2024_01_01);
        
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("base", "IDR");
        mockResponse.put("rates", historicalRates);
        
        String jsonResponse = "{\"base\":\"IDR\",\"rates\":{\"2024-01-01\":{\"USD\":0.000067}}}";
        
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
        assertInstanceOf(HistoricalRatesResponse.class, result);
        
        HistoricalRatesResponse response = (HistoricalRatesResponse) result;
        assertEquals("IDR", response.getBase());
        assertEquals("2024-01-01", response.getStartDate());
        assertEquals("2024-01-05", response.getEndDate());
        assertNotNull(response.getRates());
    }
    
    @Test
    void testGetResourceType() {
        assertEquals("historical_idr_usd", strategy.getResourceType());
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
