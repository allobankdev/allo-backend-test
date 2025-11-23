package com.allobank.aggregator.service;

import com.allobank.aggregator.dto.FinanceDataDto;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class FinanceDataStoreTest {

    @Test
    void initializeAndGetAndAll() {
        FinanceDataStore store = new FinanceDataStore();

        Map<String, Object> payload = new HashMap<>();
        payload.put("k","v");
        FinanceDataDto dto = new FinanceDataDto("supported_currencies", payload);

        Map<String, FinanceDataDto> data = new HashMap<>();
        data.put("supported_currencies", dto);
        store.initialize(data);

        assertThat(store.get("supported_currencies")).isPresent().contains(dto);
        assertThat(store.get("missing")).isEmpty();
        assertThat(store.all()).containsOnlyKeys("supported_currencies");
    }

    @Test
    void initializeTwiceShouldFail() {
        FinanceDataStore store = new FinanceDataStore();
        store.initialize(Map.of("k", new FinanceDataDto("rt", Map.of())));
        assertThatThrownBy(() -> store.initialize(Map.of()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already initialized");
    }
}
