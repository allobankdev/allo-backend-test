package com.finance.exchange.runner;

import com.finance.exchange.strategy.IDRDataFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = {
    "frankfurter.api.url=https://api.frankfurter.app",
    "github.username=testuser"
})
class FinanceDataInitializerTest {

  @MockitoBean(name = "latest_idr_rates")
  IDRDataFetcher latestRatesStrategy;

  @MockitoBean(name = "historical_idr_usd")
  IDRDataFetcher historicalStrategy;

  @MockitoBean(name = "supported_currencies")
  IDRDataFetcher currenciesStrategy;

  @Autowired
  FinanceDataInitializer initializer;

  @Test
  void testRunnerTriggersFetchDataOnStartup() {
    verify(latestRatesStrategy, times(1)).fetchData();
    verify(historicalStrategy, times(1)).fetchData();
    verify(currenciesStrategy, times(1)).fetchData();
  }
}