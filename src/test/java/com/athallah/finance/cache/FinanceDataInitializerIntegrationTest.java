package com.athallah.finance.cache;

import com.athallah.finance.util.constant.ResourceType;
import org.springframework.boot.test.context.SpringBootTest;

import com.athallah.finance.dto.LatestRatesResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FinanceDataInitializerIntegrationTest {

    @Autowired
    private FinanceDataStore dataStore;

    @Test
    void contextLoads() {
        assertThat(dataStore).isNotNull();
    }

    @Test
    void shouldLoadAllDataBeforeApplicationContextIsReady() {
        // When - application context is fully loaded
        // Then - all data should already be in the store (loaded by ApplicationRunner)

        Map<ResourceType, Object> allData = dataStore.getAllImmutable();

        assertThat(allData).isNotNull();
        assertThat(allData).hasSize(3);
        assertThat(allData).containsKeys(
                ResourceType.historical_idr_usd,
                ResourceType.latest_idr_rates,
                ResourceType.supported_currencies
        );
    }

    @Test
    void shouldLoadHistoricalIdrUsdData() {
        // Given - ApplicationRunner has executed
        // When
        Object data = dataStore.get(ResourceType.historical_idr_usd);

        // Then
        assertThat(data).isNotNull();
        assertThat(data).as("Historical IDR USD data should be loaded").isNotNull();
    }

    @Test
    void shouldLoadLatestIdrRatesData() {
        // Given - ApplicationRunner has executed
        // When
        Object data = dataStore.get(ResourceType.latest_idr_rates);

        // Then
        assertThat(data).isNotNull();
        assertThat(data).isInstanceOf(LatestRatesResponseDto.class);

        LatestRatesResponseDto response = (LatestRatesResponseDto) data;
        assertThat(response.getBase()).isEqualTo("IDR");
        assertThat(response.getRates()).isNotEmpty();
        assertThat(response.getUsdBuySpreadIdr()).isNotNull();
    }

    @Test
    void shouldLoadSupportedCurrenciesData() {
        // Given - ApplicationRunner has executed
        // When
        Object data = dataStore.get(ResourceType.supported_currencies);

        // Then
        assertThat(data).isNotNull();
        assertThat(data).isInstanceOf(Map.class);

        @SuppressWarnings("unchecked")
        Map<String, String> currencies = (Map<String, String>) data;

        assertThat(currencies).isNotEmpty();
        assertThat(currencies).containsKeys("USD", "EUR", "IDR", "GBP", "JPY");
        assertThat(currencies.get("USD")).isEqualTo("United States Dollar");
        assertThat(currencies.get("IDR")).isEqualTo("Indonesian Rupiah");
    }

    @Test
    void shouldReturnImmutableMap() {
        // Given
        Map<ResourceType, Object> allData = dataStore.getAllImmutable();

        // When/Then - attempting to modify should throw exception
        assertThat(allData).isNotNull();

        org.junit.jupiter.api.Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> allData.put(ResourceType.latest_idr_rates, "new data")
        );
    }

    @Test
    void shouldMaintainDataConsistency() {
        // Given - ApplicationRunner has executed once
        Object firstRetrieval = dataStore.get(ResourceType.latest_idr_rates);

        // When - retrieve same data again
        Object secondRetrieval = dataStore.get(ResourceType.latest_idr_rates);

        // Then - should return same instance (in-memory store)
        assertThat(firstRetrieval).isSameAs(secondRetrieval);
    }

    @Test
    void shouldHaveAllResourceTypesPopulated() {
        // Given - ApplicationRunner has executed

        // When/Then - check each ResourceType has data
        for (ResourceType type : ResourceType.values()) {
            Object data = dataStore.get(type);
            assertThat(data)
                    .as("Data for %s should be loaded by ApplicationRunner", type)
                    .isNotNull();
        }
    }
}
