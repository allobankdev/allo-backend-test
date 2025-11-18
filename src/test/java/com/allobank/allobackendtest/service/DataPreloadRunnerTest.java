package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.strategy.IdrDataFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DataPreloadRunnerTest {

    static class FakeFetcher implements IdrDataFetcher {
        private final String type;
        private final Object data;

        FakeFetcher(String type, Object data) {
            this.type = type;
            this.data = data;
        }

        @Override
        public String resourceType() {
            return type;
        }

        @Override
        public Object fetchFromApi() {
            return data;
        }
    }

    @Test
    void run_shouldLoadAllStrategiesIntoStore() throws Exception {
        // arrange
        IdrDataFetcher latest = new FakeFetcher("latest_idr_rates", Map.of("ok", true));
        IdrDataFetcher historical = new FakeFetcher("historical_idr_usd", List.of(1, 2, 3));
        IdrDataFetcher currencies = new FakeFetcher("supported_currencies", List.of("IDR", "USD"));

        InMemoryFinanceStore store = new InMemoryFinanceStore();
        DataPreloadRunner runner = new DataPreloadRunner(
                List.of(latest, historical, currencies),
                store
        );

        // act
        runner.run(new DefaultApplicationArguments(new String[]{}));

        // assert
        Object latestData = store.getByResourceType("latest_idr_rates");
        Object historicalData = store.getByResourceType("historical_idr_usd");
        Object currenciesData = store.getByResourceType("supported_currencies");

        assertThat(latestData).isEqualTo(Map.of("ok", true));
        assertThat(historicalData).isEqualTo(List.of(1, 2, 3));
        assertThat(currenciesData).isEqualTo(List.of("IDR", "USD"));
    }
}
