package com.finance.exchange.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricalRatesStrategyTest {

  @Mock
  private RestClient restClient;
  @Mock
  private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
  @Mock
  private RestClient.RequestHeadersSpec requestHeadersSpec;
  @Mock
  private RestClient.ResponseSpec responseSpec;

  private HistoricalRatesStrategy strategy;

  @BeforeEach
  void setUp() {
    strategy = new HistoricalRatesStrategy(restClient);
  }

  @Test
  void testFetchData_StoresResult() {
    when(restClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

    Map<String, Object> mockResponse = Map.of("start_date", "2024-01-01");
    when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(mockResponse);

    strategy.fetchData();

    Map<String, Object> result = (Map<String, Object>) strategy.getData();
    assertEquals("2024-01-01", result.get("start_date"));
  }
}