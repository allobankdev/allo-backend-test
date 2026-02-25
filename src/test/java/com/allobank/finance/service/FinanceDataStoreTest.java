package com.allobank.finance.service;

import com.allobank.finance.dto.FinanceDataResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;

class FinanceDataStoreTest {

    private FinanceDataStore store;

    @BeforeEach
    void setUp() {
        store = new FinanceDataStore();
    }

    @Test
    void put_andGet_shouldWorkBeforeSealing() {
        FinanceDataResponse response = FinanceDataResponse.builder()
                .resourceType("latest_idr_rates").build();

        store.put("latest_idr_rates", response);

        assertThat(store.get("latest_idr_rates")).isEqualTo(response);
    }

    @Test
    void seal_shouldPreventFurtherWrites() {
        store.seal();

        assertThatThrownBy(() -> store.put("any_key",
                FinanceDataResponse.builder().resourceType("any").build()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("sealed");
    }

    @Test
    void isSealed_shouldBeFalseByDefault() {
        assertThat(store.isSealed()).isFalse();
    }

    @Test
    void seal_shouldBeIdempotent() {
        store.seal();
        store.seal();
        assertThat(store.isSealed()).isTrue();
    }

    @Test
    void getAll_shouldReturnUnmodifiableMap() {
        store.put("latest_idr_rates", FinanceDataResponse.builder()
                .resourceType("latest_idr_rates").build());
        store.seal();

        assertThatThrownBy(() -> store.getAll().put("new_key", null))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void concurrentReads_shouldBeSafeAfterSealing() throws InterruptedException {
        store.put("latest_idr_rates", FinanceDataResponse.builder()
                .resourceType("latest_idr_rates").build());
        store.seal();

        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<FinanceDataResponse>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> store.get("latest_idr_rates")));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        for (Future<FinanceDataResponse> future : futures) {
            assertThatCode(future::get).doesNotThrowAnyException();
        }
    }
}
