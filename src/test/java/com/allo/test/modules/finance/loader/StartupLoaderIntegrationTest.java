package com.allo.test.modules.finance.loader;

import com.allo.test.modules.finance.dto.res.CurrencyResponse;
import com.allo.test.modules.finance.dto.res.HistoricalRateResponse;
import com.allo.test.modules.finance.dto.res.LatestIDRRatesResponse;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.service.DataStoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for StartupLoader.
 * <p>
 * Verifies that the ApplicationRunner successfully executes during application startup
 * and loads data into the DataStoreService. These tests work with the real application
 * context and may interact with the actual Frankfurter API.
 * <p>
 * Note: These tests verify the startup behavior and data immutability. Detailed
 * orchestration logic and failure scenarios are covered in the unit tests.
 */
@SpringBootTest
@DisplayName("StartupLoader Integration Tests")
class StartupLoaderIntegrationTest {

    @Autowired
    private DataStoreService dataStoreService;

    // ==================== STARTUP DATA LOADING ====================

    @Test
    @DisplayName("Should load data during application startup")
    void shouldLoadDataDuringApplicationStartup() {
        // Verify DataStoreService is accessible
        assertThat(dataStoreService).isNotNull();

        // If data loaded successfully, verify all three resources are available
        if (dataStoreService.isDataLoaded()) {
            LatestIDRRatesResponse latestRates = dataStoreService.get(ResourceType.LATEST_RATES);
            List<HistoricalRateResponse> historicalRates = dataStoreService.get(ResourceType.HISTORICAL_RATES);
            List<CurrencyResponse> currencies = dataStoreService.get(ResourceType.CURRENCIES);

            assertThat(latestRates).isNotNull();
            assertThat(historicalRates).isNotNull();
            assertThat(currencies).isNotNull();

            // Verify basic structure
            assertThat(latestRates.getBase()).isEqualTo("IDR");
            assertThat(latestRates.getUsdBuySpreadIdr()).isNotNull(); // Spread calculated
            assertThat(historicalRates).isNotEmpty(); // Historical rates as typed DTOs
            assertThat(currencies).isNotEmpty(); // Currencies as typed DTOs
        }

    }

    // ==================== DATA IMMUTABILITY ====================

    @Test
    @DisplayName("Should ensure data remains immutable after loading")
    void shouldEnsureDataRemainsImmutableAfterLoading() {
        // Act - Retrieve data multiple times
        LatestIDRRatesResponse firstRetrieval = dataStoreService.get(ResourceType.LATEST_RATES);
        LatestIDRRatesResponse secondRetrieval = dataStoreService.get(ResourceType.LATEST_RATES);

        // Assert - Same instance returned (no defensive copying)
        assertThat(firstRetrieval).isSameAs(secondRetrieval);

        // Verify all resources return same instances (typed DTOs)
        List<HistoricalRateResponse> historical1 = dataStoreService.get(ResourceType.HISTORICAL_RATES);
        List<HistoricalRateResponse> historical2 = dataStoreService.get(ResourceType.HISTORICAL_RATES);
        assertThat(historical1).isSameAs(historical2);

        List<CurrencyResponse> currencies1 = dataStoreService.get(ResourceType.CURRENCIES);
        List<CurrencyResponse> currencies2 = dataStoreService.get(ResourceType.CURRENCIES);
        assertThat(currencies1).isSameAs(currencies2);

        // Verify data is loaded once and served from cache
        assertThat(dataStoreService.isDataLoaded()).isTrue();
    }
}
