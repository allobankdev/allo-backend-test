package com.allobank.test;

import com.allobank.test.service.strategy.HistoricalIDRStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

class HistoricalIDRStrategyTest {

  @Test
  void testFetchData() {
    RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    HistoricalIDRStrategy strategy = new HistoricalIDRStrategy(restTemplate);

    Map<String, Object> mockResponse = Map.of(
        "start_date", "2024-01-01",
        "end_date", "2024-01-05",
        "rates", Map.of("2024-01-01", Map.of("USD", 0.0001)));

    Mockito.when(restTemplate.getForObject(anyString(), eq(Map.class)))
        .thenReturn(mockResponse);

    Object result = strategy.fetchData();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(mockResponse, result);

    Mockito.verify(restTemplate).getForObject("/2024-01-01..2024-01-05?from=IDR&to=USD", Map.class);
  }
}