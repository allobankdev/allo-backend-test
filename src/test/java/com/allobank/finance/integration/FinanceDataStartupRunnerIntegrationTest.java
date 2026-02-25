package com.allobank.finance.integration;

import com.allobank.finance.dto.FinanceDataResponse;
import com.allobank.finance.repository.FinanceDataCacheRepository;
import com.allobank.finance.service.FinanceDataStore;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class FinanceDataStartupRunnerIntegrationTest {

    static WireMockServer wireMockServer;

    @Autowired
    private FinanceDataStore financeDataStore;

    @Autowired
    private FinanceDataCacheRepository cacheRepository;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/latest?base=IDR"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "amount": 1.0,
                              "base": "IDR",
                              "date": "2024-01-05",
                              "rates": {
                                "USD": 0.000064,
                                "EUR": 0.000059,
                                "SGD": 0.000086
                              }
                            }
                            """)));

        wireMockServer.stubFor(get(urlEqualTo("/2024-01-01..2024-01-05?from=IDR&to=USD"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "amount": 1.0,
                              "base": "IDR",
                              "start_date": "2024-01-01",
                              "end_date": "2024-01-05",
                              "rates": {
                                "2024-01-02": { "USD": 0.000064 },
                                "2024-01-03": { "USD": 0.000065 },
                                "2024-01-05": { "USD": 0.000063 }
                              }
                            }
                            """)));

        wireMockServer.stubFor(get(urlEqualTo("/currencies"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "AUD": "Australian Dollar",
                              "EUR": "Euro",
                              "IDR": "Indonesian Rupiah",
                              "SGD": "Singapore Dollar",
                              "USD": "US Dollar"
                            }
                            """)));
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) wireMockServer.stop();
    }

    @DynamicPropertySource
    static void overrideFrankfurterBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("frankfurter.base-url", () -> "http://localhost:" + wireMockServer.port());
    }

    @Test
    void contextLoads_andStoreIsSealed() {
        assertThat(financeDataStore.isSealed()).isTrue();
    }

    @Test
    void allThreeResourceTypes_shouldBeLoadedOnStartup() {
        assertThat(financeDataStore.containsKey("latest_idr_rates"))
                .as("latest_idr_rates should be loaded").isTrue();
        assertThat(financeDataStore.containsKey("historical_idr_usd"))
                .as("historical_idr_usd should be loaded").isTrue();
        assertThat(financeDataStore.containsKey("supported_currencies"))
                .as("supported_currencies should be loaded").isTrue();
    }

    @Test
    void latestIdrRates_shouldContainSpreadCalculation() {
        FinanceDataResponse latestRates = financeDataStore.get("latest_idr_rates");

        assertThat(latestRates).isNotNull();
        assertThat(latestRates.getUsdBuySpreadIdr())
                .as("USD_BuySpread_IDR should be calculated")
                .isNotNull()
                .isPositive();
        assertThat(latestRates.getSpreadFactor())
                .as("Spread factor should be 0.00264 for 'thaufaniqbal'")
                .isEqualTo(0.00264);

        double expected = (1.0 / 0.000064) * (1.0 + 0.00264);
        assertThat(latestRates.getUsdBuySpreadIdr())
                .isCloseTo(expected, within(0.01));
    }

    @Test
    void historicalIdrUsd_shouldNotContainSpread() {
        FinanceDataResponse historical = financeDataStore.get("historical_idr_usd");
        assertThat(historical).isNotNull();
        assertThat(historical.getUsdBuySpreadIdr()).isNull();
    }

    @Test
    void supportedCurrencies_shouldBeNonEmptyMap() {
        FinanceDataResponse currencies = financeDataStore.get("supported_currencies");
        assertThat(currencies).isNotNull();
        assertThat(currencies.getUsdBuySpreadIdr()).isNull();
        assertThat(currencies.getData()).isInstanceOf(Map.class);
        @SuppressWarnings("unchecked")
        Map<String, String> data = (Map<String, String>) currencies.getData();
        assertThat(data).containsKey("IDR").containsKey("USD");
    }

    @Test
    void allThreeResources_shouldBePersistedToDatabase() {
        assertThat(cacheRepository.findByResourceType("latest_idr_rates")).isPresent();
        assertThat(cacheRepository.findByResourceType("historical_idr_usd")).isPresent();
        assertThat(cacheRepository.findByResourceType("supported_currencies")).isPresent();
    }

    @Test
    void sealedStore_shouldRejectNewWrites() {
        assertThat(financeDataStore.isSealed()).isTrue();
        assertThatThrownBy(() ->
                financeDataStore.put("test_resource",
                        FinanceDataResponse.builder().resourceType("test").build()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("sealed");
    }

    @Test
    void wireMock_shouldHaveBeenCalledForAllThreeEndpoints() {
        wireMockServer.verify(getRequestedFor(urlEqualTo("/latest?base=IDR")));
        wireMockServer.verify(getRequestedFor(urlEqualTo("/2024-01-01..2024-01-05?from=IDR&to=USD")));
        wireMockServer.verify(getRequestedFor(urlEqualTo("/currencies")));
    }
}
