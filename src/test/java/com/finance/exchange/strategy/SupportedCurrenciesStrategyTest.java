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
class SupportedCurrenciesStrategyTest {

  @Mock
  private RestClient restClient;
  @Mock
  private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
  @Mock
  private RestClient.RequestHeadersSpec requestHeadersSpec;
  @Mock
  private RestClient.ResponseSpec responseSpec;

  private SupportedCurrenciesStrategy strategy;

  @BeforeEach
  void setUp() {
    strategy = new SupportedCurrenciesStrategy(restClient);
  }

  @Test
  void testFetchData_StoresCurrencies() {
    when(restClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

    Map<String, String> mockResponse = Map.of("USD", "United States Dollar");
    when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(mockResponse);

    strategy.fetchData();

    Map<String, String> result = (Map<String, String>) strategy.getData();
    assertEquals("United States Dollar", result.get("USD"));
  }
}