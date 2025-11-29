package com.allobank.backendtest.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class ImmutableFinanceStoreTest {
    @Test
    public void initialize_once_and_immutable() {
        ImmutableFinanceStore store = new ImmutableFinanceStore();
        Map<String, List<?>> data = Map.of(
                "a", List.of(1,2,3),
                "b", List.of("x")
        );
        store.initialize(data);

        assertTrue(store.isInitialized());
        List<?> a = store.get("a");
        assertThrows(UnsupportedOperationException.class, () -> ((List)a).add(99));

        // double init should fail
        assertThrows(IllegalStateException.class, () -> store.initialize(Map.of()));
    }
}
