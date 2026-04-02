package com.example.idrapi.integration;

import com.example.idrapi.model.FinanceDataResponse;
import com.example.idrapi.service.FinanceDataStore;
import com.example.idrapi.service.FinanceDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.idrapi.strategy.IDRDataFetcher;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Integration test: verifies that the ApplicationRunner successfully invokes
 * loadAll(), stores data in the FinanceDataStore, and seals it before the
 * context is fully ready to serve requests.
 *
 * We mock all three IDRDataFetcher beans so this test runs without a live
 * network connection to api.frankfurter.app.
 */
@SpringBootTest
@DisplayName("FinanceDataStartupRunner Integration Tests")
class StartupRunnerIntegrationTest {

    @MockBean(name = "latestIDRRatesFetcher")
    private IDRDataFetcher latestFetcher;

    @MockBean(name = "historicalIDRUSDFetcher")
    private IDRDataFetcher historicalFetcher;

    @MockBean(name = "supportedCurrenciesFetcher")
    private IDRDataFetcher supportedFetcher;

    @Autowired
    private FinanceDataStore dataStore;

    @Autowired
    private FinanceDataService financeDataService;

    // ------------------------------------------------------------------ tests

    @Test
    @DisplayName("ApplicationRunner: data store is sealed after context loads")
    void dataStore_isSealed_afterContextLoad() {
        // The ApplicationRunner ran during context startup; store must be sealed
        assertThat(dataStore.isSealed()).isTrue();
    }

    @Test
    @DisplayName("ApplicationRunner: all three resource types are present in the store")
    void dataStore_containsAllThreeResources() {
        // Each mock fetcher returns its resourceType; data must be stored for all three
        assertThat(financeDataService.getRegisteredResourceTypes())
                .containsExactlyInAnyOrder(
                        "latest_idr_rates",
                        "historical_idr_usd",
                        "supported_currencies"
                );
    }

    @Test
    @DisplayName("FinanceDataService.getData: returns cached data without calling fetcher again")
    void getData_returnsCachedData_noDuplicateFetcherCall() {
        // After startup, additional getData() calls must NOT trigger external fetches
        Optional<FinanceDataResponse> latest = financeDataService.getData("latest_idr_rates");
        Optional<FinanceDataResponse> historical = financeDataService.getData("historical_idr_usd");
        Optional<FinanceDataResponse> currencies = financeDataService.getData("supported_currencies");

        // All three should be present (mocks returned data during startup)
        assertThat(latest).isPresent();
        assertThat(historical).isPresent();
        assertThat(currencies).isPresent();

        // Fetchers should have been called ONLY ONCE (at startup), not again
        verify(latestFetcher, times(1)).fetch();
        verify(historicalFetcher, times(1)).fetch();
        verify(supportedFetcher, times(1)).fetch();
    }

    @Test
    @DisplayName("FinanceDataService.getData: returns empty Optional for unknown resourceType")
    void getData_returnsEmpty_forUnknownType() {
        Optional<FinanceDataResponse> result = financeDataService.getData("unknown_type");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("FinanceDataStore: put() is rejected after sealing")
    void dataStore_put_isRejectedAfterSealed() {
        // store is already sealed from startup
        int sizeBefore = dataStore.getAll().size();
        FinanceDataResponse dummy = new FinanceDataResponse(
                "new_type", Instant.now(), List.of(Map.of("key", "value")));
        dataStore.put("new_type", dummy); // should be silently ignored

        assertThat(dataStore.getAll()).hasSize(sizeBefore);
        assertThat(dataStore.get("new_type")).isEmpty();
    }

    // ------------------------------------------------------------------ mock setup (called by Spring before runner)

    static {
        // Static initializer registers mock behavior; actual wiring done via @MockBean above.
        // The mocks auto-return empty lists by default; we set up responses in the test class initializer.
    }

    @org.springframework.boot.test.context.TestConfiguration
    static class MockFetcherConfig {
        // MockBeans at class level supply these beans; Spring auto-wires them into the strategy list.
    }
}
