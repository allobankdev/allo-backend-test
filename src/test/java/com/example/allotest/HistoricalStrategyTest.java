package com.example.allotest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.allotest.service.DataStoreService;
import com.example.allotest.strategy.impl.HistoricalStrategy;

class HistoricalStrategyTest {

    @Test
    void shouldReturnHistoricalData() {
        DataStoreService store = new DataStoreService();

        store.save("historical_idr_usd", Map.of(
            "base", "IDR",
            "rates", Map.of(
            )
        ));

        HistoricalStrategy strategy = new HistoricalStrategy(store);
        Object result = strategy.getData();
        assertNotNull(result);
    }

}