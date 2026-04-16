package com.allobank.finance.runner;

import com.allobank.finance.store.FinanceDataStore;
import com.allobank.finance.strategy.IDRDataFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FinanceDataInitializerTest {

    @Test
    @SuppressWarnings("unchecked")
    void applicationRunnerLoadsEveryStrategyIntoImmutableStore() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        FinanceDataStore store = new FinanceDataStore();
        Map<String, IDRDataFetcher> fetchers = Map.of(
                "latest_idr_rates", () -> {
                    calls.incrementAndGet();
                    return List.of(Map.of("resource", "latest", "rates", Map.of("USD", 0.000064)));
                },
                "historical_idr_usd", () -> {
                    calls.incrementAndGet();
                    return List.of(Map.of("resource", "history"));
                },
                "supported_currencies", () -> {
                    calls.incrementAndGet();
                    return List.of(Map.of("resource", "currencies"));
                });

        new FinanceDataInitializer(fetchers, store)
                .dataInitializerRunner()
                .run(new DefaultApplicationArguments());

        assertThat(calls).hasValue(3);
        assertThat(store.getData("latest_idr_rates")).isPresent();
        assertThatThrownBy(() -> store.getData("latest_idr_rates").orElseThrow().add(Map.of()))
                .isInstanceOf(UnsupportedOperationException.class);
        Map<String, Object> firstRow = store.getData("latest_idr_rates").orElseThrow().get(0);
        assertThatThrownBy(() -> ((Map<String, Object>) firstRow.get("rates")).put("EUR", 0.000059))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
