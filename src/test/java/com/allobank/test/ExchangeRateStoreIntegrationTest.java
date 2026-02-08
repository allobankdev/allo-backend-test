package com.allobank.test;

import com.allobank.test.dto.ExchangeRateResponse;
import com.allobank.test.service.ExchangeRateStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;

import java.util.Map;

@SpringBootTest
class ExchangeRateStoreIntegrationTest {

  @Autowired
  private ExchangeRateStore store;

  @MockBean
  private RestTemplate restTemplate;

  @Test
  void testApplicationStartupLoadsData() {
    ExchangeRateResponse mockLatest = new ExchangeRateResponse();
    mockLatest.setRates(Map.of("USD", 0.0001));
    mockLatest.setBase("IDR");

    Mockito.when(restTemplate.getForObject(any(String.class), eq(ExchangeRateResponse.class)))
        .thenReturn(mockLatest);

    Mockito.when(restTemplate.getForObject(any(String.class), eq(Map.class)))
        .thenReturn(Map.of("mock", "data"));

    store.run(null);

    Object latestData = store.getData("latest_idr_rates");
    Object historyData = store.getData("historical_idr_usd");
    Object currencyData = store.getData("supported_currencies");

    Assertions.assertNotNull(latestData, "Store should contain latest_idr_rates");
    Assertions.assertNotNull(historyData, "Store should contain historical_idr_usd");
    Assertions.assertNotNull(currencyData, "Store should contain supported_currencies");
  }
}