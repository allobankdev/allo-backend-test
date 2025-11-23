package com.allobank.aggregator.runner;

import com.allobank.aggregator.dto.FinanceDataDto;
import com.allobank.aggregator.service.FinanceDataStore;
import com.allobank.aggregator.strategy.IDRDataFetcher;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class FinanceDataLoaderTest {

    @Test
    void run_loadsAllAndInitializesStore() throws Exception {
        IDRDataFetcher f1 = new DummyFetcher("a", Map.of("x", 1));
        IDRDataFetcher f2 = new DummyFetcher("b", Map.of("y", 2));
        FinanceDataStore store = new FinanceDataStore();
        FinanceDataLoader loader = new FinanceDataLoader(List.of(f1, f2), store);

        loader.run(null);

        assertThat(store.all()).containsOnlyKeys("a", "b");
        assertThat(store.get("a")).isPresent();
        assertThat(store.get("b")).isPresent();
    }

    @Test
    void run_whenAnyFetcherFails_throwsRuntimeException() {
        IDRDataFetcher ok = new DummyFetcher("ok", Map.of());
        IDRDataFetcher fail = new IDRDataFetcher() {
            @Override public String resourceKey() { return "bad"; }
            @Override public FinanceDataDto fetch() { throw new IllegalStateException("boom"); }
        };
        FinanceDataStore store = new FinanceDataStore();
        FinanceDataLoader loader = new FinanceDataLoader(List.of(ok, fail), store);

        assertThatThrownBy(() -> loader.run(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Initialization failed for bad");
    }

    private static class DummyFetcher implements IDRDataFetcher {
        private final String key; private final Map<String, Object> payload;
        private DummyFetcher(String key, Map<String, Object> payload) { this.key = key; this.payload = payload; }
        @Override public String resourceKey() { return key; }
        @Override public FinanceDataDto fetch() { return new FinanceDataDto(key, payload); }
    }
}
