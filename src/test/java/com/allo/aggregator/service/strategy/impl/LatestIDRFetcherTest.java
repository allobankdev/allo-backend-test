package com.allo.aggregator.service.strategy.impl;

import com.allo.aggregator.config.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LatestIDRFetcherTest {

  @Mock
  private WebClient webClient;

  @Mock
  private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

  @Mock
  private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

  @Mock
  private WebClient.ResponseSpec responseSpec;

  @Mock
  private AppConfig appConfig;

  private LatestIDRFetcher fetcher;

  @BeforeEach
  void setUp() {
    fetcher = new LatestIDRFetcher(webClient, appConfig);
  }

  @Test
  void fetchData_shouldCalculateSpreadCorrectly() {
    // Mock Config
    AppConfig.Spread spread = new AppConfig.Spread();
    spread.setUsername("halim13");
    when(appConfig.getSpread()).thenReturn(spread);

    // Mock WebClient chain
    doReturn(requestHeadersUriSpec).when(webClient).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

    Map<String, Object> mockResponse = new HashMap<>();
    Map<String, Object> rates = new HashMap<>();
    rates.put("USD", 0.000064); // Example rate: 1 IDR = 0.000064 USD (Approx ~15600 IDR/USD)
    mockResponse.put("rates", rates);

    doReturn(Mono.just(mockResponse)).when(responseSpec)
        .bodyToMono(org.mockito.ArgumentMatchers.<ParameterizedTypeReference<Map<String, Object>>>any());

    // Execute
    Map<String, Object> result = fetcher.fetchData();

    // Verify
    assertNotNull(result);
    assertTrue(result.containsKey("USD_BuySpread_IDR"));

    // Calculation check
    // Username "halim13" -> sum 623 -> factor 0.00623
    // Rate USD = 0.000064
    // Formula: (1 / 0.000064) * (1 + 0.00623)
    // 15625 * 1.00623 = 15722.34375

    double expectedSpreadFactor = 0.00623;
    double inputRate = 0.000064;
    double expectedValue = (1.0 / inputRate) * (1.0 + expectedSpreadFactor);

    assertEquals(expectedValue, ((BigDecimal) result.get("USD_BuySpread_IDR")).doubleValue(), 0.0001);
    assertEquals(expectedSpreadFactor, (Double) result.get("Spread_Factor_Used"), 0.00001);
  }
}
