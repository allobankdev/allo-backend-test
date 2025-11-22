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
class InitialFinanceDataRunnerIntegrationTest {

  private static WireMockServer wireMockServer;

  @Autowired
  private InMemoryRepository inMemoryRepository;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    wireMockServer = new WireMockServer(
      WireMockConfiguration.wireMockConfig()
        .dynamicPort()
        .usingFilesUnderClasspath("wiremock")
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
  void onStartup_whenSuccessfullyFetchFromFrankfurterApi_shouldProperlySetDataToInMemory() {
    await()
      .atMost(Duration.ofSeconds(5))
      .pollInterval(Duration.ofMillis(100))
      .untilAsserted(() -> {
        LatestRatesResponse latestRates = (LatestRatesResponse) inMemoryRepository
          .get(ResourceTypeConstant.LATEST_IDR_RATES)
          .block();
        assertThat(latestRates).isNotNull();
        assertThat(latestRates.base()).isEqualTo("IDR");
        assertThat(latestRates.date()).isEqualTo("2024-01-10");
        assertThat(latestRates.rates()).containsKeys("USD", "EUR", "GBP", "SGD", "JPY");
        assertThat(latestRates.rates().get("USD")).isEqualTo(0.000063);
        assertThat(latestRates.rates().get("EUR")).isEqualTo(0.000058);

        HistoricalRatesResponse historicalRates = (HistoricalRatesResponse) inMemoryRepository
          .get(ResourceTypeConstant.HISTORICAL_IDR_USD)
          .block();
        assertThat(historicalRates).isNotNull();
        assertThat(historicalRates.base()).isEqualTo("IDR");
        assertThat(historicalRates.startDate()).isEqualTo("2024-01-01");
        assertThat(historicalRates.endDate()).isEqualTo("2024-01-05");
        assertThat(historicalRates.rates()).hasSize(5);
        assertThat(historicalRates.rates()).containsKeys(
          "2024-01-01", "2024-01-02", "2024-01-03", "2024-01-04", "2024-01-05"
        );
        assertThat(historicalRates.rates().get("2024-01-01").get("USD")).isEqualTo(0.000062);
        assertThat(historicalRates.rates().get("2024-01-05").get("USD")).isEqualTo(0.000064);

        Map<String, String> currencies = (Map<String, String>) inMemoryRepository
          .get(ResourceTypeConstant.SUPPORTED_CURRENCIES)
          .block();
        assertThat(currencies).isNotNull();
        assertThat(currencies).isNotEmpty();
        assertThat(currencies).containsKeys("USD", "EUR", "GBP", "JPY", "SGD", "IDR");
        assertThat(currencies).hasSize(31);
      });
  }
}

