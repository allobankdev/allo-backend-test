package com.allobank.test;

import com.allobank.test.dto.ExchangeRateResponse;
import com.allobank.test.service.strategy.LatestIDRRatesStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

class LatestIDRRatesStrategyTest {

  @Test
  void testSpreadCalculationLogic() {
    RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    LatestIDRRatesStrategy strategy = new LatestIDRRatesStrategy(restTemplate, "abc");

    ExchangeRateResponse mockResponse = new ExchangeRateResponse();
    mockResponse.setRates(Map.of("USD", 0.0001));

    Mockito.when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class)))
        .thenReturn(mockResponse);

    Map<String, Object> result = (Map<String, Object>) strategy.fetchData();

    Assertions.assertEquals(10029.4, (Double) result.get("USD_BuySpread_IDR"), 0.001);
  }
}