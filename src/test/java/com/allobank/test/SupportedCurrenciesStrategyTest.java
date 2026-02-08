package com.allobank.test;

import com.allobank.test.service.strategy.SupportedCurrenciesStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

class SupportedCurrenciesStrategyTest {

  @Test
  void testFetchData() {
    RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    SupportedCurrenciesStrategy strategy = new SupportedCurrenciesStrategy(restTemplate);

    Map<String, String> mockResponse = Map.of("USD", "United States Dollar", "IDR", "Indonesian Rupiah");

    Mockito.when(restTemplate.getForObject(anyString(), eq(Map.class)))
        .thenReturn(mockResponse);

    Object result = strategy.fetchData();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(mockResponse, result);
    Mockito.verify(restTemplate).getForObject("/currencies", Map.class);
  }
}