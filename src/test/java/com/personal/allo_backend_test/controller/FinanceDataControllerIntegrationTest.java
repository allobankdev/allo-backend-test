package com.personal.allo_backend_test.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.personal.allo_backend_test.constant.ResourceTypeConstant;
import com.personal.allo_backend_test.constant.ResponseConstant;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FinanceDataControllerIntegrationTest {

  private static WireMockServer wireMockServer;

  @Autowired
  private WebTestClient webTestClient;

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
  void fetchData_whenLatestIdrRates_shouldReturnSuccessWithData() {
    double expectedUsdBuySpreadIdr = 15874.761905;
    double tolerance = 0.000001;

    Map<String, Object> response = webTestClient.get()
      .uri("/api/finance/data/{resourceType}", ResourceTypeConstant.LATEST_IDR_RATES)
      .exchange()
      .expectStatus().isOk()
      .expectBody(Map.class)
      .returnResult()
      .getResponseBody();

    assertThat(response.get("status")).isEqualTo(ResponseConstant.STATUS_SUCCESS);
    assertThat(response.get("resourceType")).isEqualTo(ResourceTypeConstant.LATEST_IDR_RATES);
    assertThat(response.get("data")).isInstanceOf(List.class);

    List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
    assertThat(data).isNotEmpty();

    Optional<Map<String, Object>> usdEntry = data.stream()
      .filter(entry -> "USD".equals(entry.get("code")))
      .findFirst();

    assertThat(usdEntry).isPresent();
    assertThat(usdEntry.get().get("rate")).isEqualTo(0.000063);
    assertThat(usdEntry.get().get("usdBuySpreadIdr")).isNotNull();

    double actualUsdBuySpreadIdr = ((Number) usdEntry.get().get("usdBuySpreadIdr")).doubleValue();
    assertThat(actualUsdBuySpreadIdr).isCloseTo(expectedUsdBuySpreadIdr, org.assertj.core.data.Offset.offset(tolerance));
  }

  @Test
  void fetchData_whenHistoricalIdrUsd_shouldReturnSuccessWithData() {
    webTestClient.get()
      .uri("/api/finance/data/{resourceType}", ResourceTypeConstant.HISTORICAL_IDR_USD)
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.status").isEqualTo(ResponseConstant.STATUS_SUCCESS)
      .jsonPath("$.resourceType").isEqualTo(ResourceTypeConstant.HISTORICAL_IDR_USD)
      .jsonPath("$.data").isArray()
      .jsonPath("$.data").isNotEmpty()
      .jsonPath("$.data[0].date").exists()
      .jsonPath("$.data[0].rate").isNumber()
      .consumeWith(result -> {
        String body = new String(result.getResponseBody());
        assertThat(body).contains("2024-01-01", "2024-01-02", "2024-01-03", "2024-01-04", "2024-01-05");
      });
  }

  @Test
  void fetchData_whenSupportedCurrencies_shouldReturnSuccessWithData() {
    webTestClient.get()
      .uri("/api/finance/data/{resourceType}", ResourceTypeConstant.SUPPORTED_CURRENCIES)
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.status").isEqualTo(ResponseConstant.STATUS_SUCCESS)
      .jsonPath("$.resourceType").isEqualTo(ResourceTypeConstant.SUPPORTED_CURRENCIES)
      .jsonPath("$.data").isArray()
      .jsonPath("$.data").isNotEmpty()
      .jsonPath("$.data[0].code").exists()
      .jsonPath("$.data[0].name").exists()
      .consumeWith(result -> {
        String body = new String(result.getResponseBody());
        assertThat(body).contains("USD", "EUR", "GBP", "JPY", "IDR");
      });
  }

  @Test
  void fetchData_whenUnsupportedResourceType_shouldReturnBadRequest() {
    String unsupportedResourceType = "invalid_resource_type";

    webTestClient.get()
      .uri("/api/finance/data/{resourceType}", unsupportedResourceType)
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$.status").isEqualTo(ResponseConstant.STATUS_FAILED)
      .jsonPath("$.message").value(message -> {
        assertThat(message.toString()).contains(unsupportedResourceType);
        assertThat(message.toString()).contains("not supported");
      })
      .jsonPath("$.data").isArray()
      .jsonPath("$.data").isEmpty();
  }
}

