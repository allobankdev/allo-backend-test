package com.allobank.finace.runner;

import com.allobank.finace.service.DataStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DataStartupRunnerIntegrationTest {

    @Autowired
    private DataStoreService dataStoreService;

    @Test
    void applicationRunner_shouldLoadAllThreeResources() {
        assertThat(dataStoreService.contains("latest_idr_rates")).isTrue();
        assertThat(dataStoreService.contains("historical_idr_usd")).isTrue();
        assertThat(dataStoreService.contains("supported_currencies")).isTrue();
    }

    @Test
    void latestIdrRates_shouldContainUsdBuySpreadIdr() {
        assertThat(dataStoreService.get("latest_idr_rates")).isNotEmpty();
        assertThat(dataStoreService.get("latest_idr_rates").get(0))
                .containsKey("USD_BuySpread_IDR");
    }

    @Test
    void historicalIdrUsd_shouldContainDateEntries() {
        assertThat(dataStoreService.get("historical_idr_usd")).isNotEmpty();
        assertThat(dataStoreService.get("historical_idr_usd").get(0))
                .containsKey("date");
    }

    @Test
    void supportedCurrencies_shouldContainCodeAndName() {
        assertThat(dataStoreService.get("supported_currencies")).isNotEmpty();
        assertThat(dataStoreService.get("supported_currencies").get(0))
                .containsKeys("code", "name");
    }
}
