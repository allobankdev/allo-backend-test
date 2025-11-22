package com.personal.allo_backend_test.runner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.personal.allo_backend_test.client.response.HistoricalRatesResponse;
import com.personal.allo_backend_test.client.response.LatestRatesResponse;
import com.personal.allo_backend_test.constant.ResourceTypeConstant;
import com.personal.allo_backend_test.repository.InMemoryRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InitialFinanceDataRunner4XxErrorIntegrationTest {

  private static WireMockServer wireMockServer;

  @Autowired
  private InMemoryRepository inMemoryRepository;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    wireMockServer = new WireMockServer(
      WireMockConfiguration.wireMockConfig()
        .dynamicPort()
        .usingFilesUnderClasspath("wiremock-errors-4xx")
    );
    wireMockServer.start();
    registry.add("client.frankfurter.base-url", wireMockServer::baseUrl);
  }

  @AfterEach
  void tearDown() {
    Optional.ofNullable(wireMockServer)
      .filter(WireMockServer::isRunning)
      .ifPresent(WireMockServer::stop);
  }

  @Test
  void onStartup_when4xxErrorsFromFrankfurterApi_shouldStoreDefaultDtosInMemory() {
    await()
      .atMost(Duration.ofSeconds(5))
      .pollInterval(Duration.ofMillis(100))
      .untilAsserted(() -> {
        LatestRatesResponse latestRates = (LatestRatesResponse) inMemoryRepository
          .get(ResourceTypeConstant.LATEST_IDR_RATES)
          .block();
        assertThat(latestRates).isNotNull();
        assertThat(latestRates.base()).isNull();
        assertThat(latestRates.date()).isNull();
        assertThat(latestRates.amount()).isNull();
        assertThat(latestRates.rates()).isNull();

        HistoricalRatesResponse historicalRates = (HistoricalRatesResponse) inMemoryRepository
          .get(ResourceTypeConstant.HISTORICAL_IDR_USD)
          .block();
        assertThat(historicalRates).isNotNull();
        assertThat(historicalRates.base()).isNull();
        assertThat(historicalRates.startDate()).isNull();
        assertThat(historicalRates.endDate()).isNull();
        assertThat(historicalRates.amount()).isNull();
        assertThat(historicalRates.rates()).isNull();

        Map<String, String> currencies = (Map<String, String>) inMemoryRepository
          .get(ResourceTypeConstant.SUPPORTED_CURRENCIES)
          .block();
        assertThat(currencies).isNotNull();
        assertThat(currencies).isEmpty();
      });
  }
}

