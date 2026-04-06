package com.example.allotest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.allotest.service.DataStoreService;
import com.example.allotest.strategy.impl.CurrencyStrategy;

class CurrencyStrategyTest {
    
    @Test
    void shouldReturnCurrencyData() {
        DataStoreService store = new DataStoreService();
        store.save("supported_currencies", Map.of(
            "USD", "United States Dollar",
            "EUR", "Euro",
            "JPY", "Japanese Yen"
        ));

        CurrencyStrategy strategy = new CurrencyStrategy(store);
        Object result = strategy.getData();
        assertNotNull(result);
    }
}
