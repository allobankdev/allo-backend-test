package com.allobank.idr_rate_aggregator.runner;

import com.allobank.idr_rate_aggregator.model.FinanceData;
import com.allobank.idr_rate_aggregator.store.FinanceDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DataInitializerRunnerTest {

    @Autowired
    private FinanceDataStore dataStore;

    @Test
    void applicationRunner_shouldLoadAllThreeResources() {
        assertThat(dataStore.isLoaded()).isTrue();
        assertThat(dataStore.getAll()).hasSize(3);
    }

    @Test
    void applicationRunner_shouldLoadLatestIdrRates() {
        FinanceData data = dataStore.get("latest_idr_rates");
        assertThat(data).isNotNull();
        assertThat(data.getResourceType()).isEqualTo("latest_idr_rates");
        assertThat(data.getData()).isNotNull();
    }

    @Test
    void applicationRunner_shouldLoadHistoricalIdrUsd() {
        FinanceData data = dataStore.get("historical_idr_usd");
        assertThat(data).isNotNull();
        assertThat(data.getResourceType()).isEqualTo("historical_idr_usd");
    }

    @Test
    void applicationRunner_shouldLoadSupportedCurrencies() {
        FinanceData data = dataStore.get("supported_currencies");
        assertThat(data).isNotNull();
        assertThat(data.getResourceType()).isEqualTo("supported_currencies");
    }

    @Test
    void applicationRunner_shouldContainUsdBuySpreadInLatestRates() {
        FinanceData data = dataStore.get("latest_idr_rates");
        assertThat(data).isNotNull();

        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> latestData = (java.util.Map<String, Object>) data.getData();
        assertThat(latestData).containsKey("USD_BuySpread_IDR");

        double spread = ((Number) latestData.get("USD_BuySpread_IDR")).doubleValue();
        assertThat(spread).isGreaterThan(0);
    }
}
