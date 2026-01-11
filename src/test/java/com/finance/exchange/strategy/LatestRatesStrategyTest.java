package com.finance.exchange.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestRatesStrategyTest {

  @Mock
  private RestClient restClient;

  @Mock
  private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

  @Mock
  private RestClient.RequestHeadersSpec requestHeadersSpec;

  @Mock
  private RestClient.ResponseSpec responseSpec;

  private LatestRatesStrategy strategy;

  @BeforeEach
  void setUp() {
    strategy = new LatestRatesStrategy(restClient);
    ReflectionTestUtils.setField(strategy, "githubUsername", "testuser");
  }

  @Test
  void testFetchData_CalculatesSpreadCorrectly() {
    when(restClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

    Map<String, Object> mockApiResponse = new HashMap<>();
    Map<String, Double> rates = new HashMap<>();
    rates.put("USD", 0.0001);
    mockApiResponse.put("rates", rates);

    when(responseSpec.body(any(ParameterizedTypeReference.class)))
        .thenReturn(mockApiResponse);

    strategy.fetchData();
    Map<String, Object> result = (Map<String, Object>) strategy.getData();

    assertNotNull(result);
    assertTrue(result.containsKey("USD_BuySpread_IDR"));

    double calculatedSpread = (double) result.get("USD_BuySpread_IDR");
    assertTrue(calculatedSpread > 0);
  }
}