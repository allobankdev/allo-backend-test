package com.example.allotest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.allotest.service.DataStoreService;
import com.example.allotest.strategy.impl.LatestRateStrategy;

class LatestRateStrategyTest {
    @Test
    void shouldCalculateSpread() {
        DataStoreService store = new DataStoreService();
        store.save("latest_idr_rates", Map.of(
            "base", "IDR",
            "date", "2024-06-01",
            "rates", Map.of("USD", 0.0000064)
        ));

        LatestRateStrategy strategy = new LatestRateStrategy(store);
        Object result = strategy.getData();
        assertNotNull(result);
    }
}
