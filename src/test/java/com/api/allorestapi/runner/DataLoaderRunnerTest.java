package com.api.allorestapi.runner;

import com.api.allorestapi.model.FinanceDataResponse;
import com.api.allorestapi.store.FinanceDataStore;
import com.api.allorestapi.strategy.IDRDataFetch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataLoaderRunner Integration Tests")
class DataLoaderRunnerTest {

    @Mock private IDRDataFetch latestFetcher;
    @Mock private IDRDataFetch historicalFetcher;
    @Mock private IDRDataFetch currenciesFetcher;

    private FinanceDataStore store;
    private DataLoaderRunner runner;

    @BeforeEach
    void setUp() {
        // Configure mocks BEFORE runner is created
        when(latestFetcher.getResourceType()).thenReturn("latest_idr_rates");
        when(latestFetcher.fetch()).thenReturn(Mono.just(
                FinanceDataResponse.builder().resourceType("latest_idr_rates").data(List.of()).build()));

        when(historicalFetcher.getResourceType()).thenReturn("historical_idr_usd");
        when(historicalFetcher.fetch()).thenReturn(Mono.just(
                FinanceDataResponse.builder().resourceType("historical_idr_usd").data(List.of()).build()));

        when(currenciesFetcher.getResourceType()).thenReturn("supported_currencies");
        when(currenciesFetcher.fetch()).thenReturn(Mono.just(
                FinanceDataResponse.builder().resourceType("supported_currencies").data(List.of()).build()));

        store = new FinanceDataStore();
        runner = new DataLoaderRunner(List.of(latestFetcher, historicalFetcher, currenciesFetcher), store);

        runner.run(null);
    }

    @Test
    @DisplayName("Store is loaded after runner executes")
    void store_isLoadedAfterRunnerExecutes() {
        assertThat(store.isLoaded()).isTrue();
    }

    @Test
    @DisplayName("Store is populated for all three resource types after startup")
    void store_isPopulatedForAllThreeResources() {
        assertThat(store.get("latest_idr_rates")).isNotNull();
        assertThat(store.get("historical_idr_usd")).isNotNull();
        assertThat(store.get("supported_currencies")).isNotNull();
    }

    @Test
    @DisplayName("Store returns correct resourceType for each key")
    void store_returnsCorrectResourceTypePerKey() {
        assertThat(store.get("latest_idr_rates").getResourceType()).isEqualTo("latest_idr_rates");
        assertThat(store.get("historical_idr_usd").getResourceType()).isEqualTo("historical_idr_usd");
        assertThat(store.get("supported_currencies").getResourceType()).isEqualTo("supported_currencies");
    }

    @Test
    @DisplayName("Store returns null for unknown resource type")
    void store_returnsNullForUnknownKey() {
        assertThat(store.get("unknown_resource")).isNull();
    }
}