package com.zultest.allobank_backend_test.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryStoreTest {

    private InMemoryStore store;

    @BeforeEach
    void setup() {
        store = new InMemoryStore();
    }

    @Test
    void shouldStoreAndRetrieveData() {
        store.put("latest_idr_rates", "data");
        store.markInitialized();

        assertEquals("data", store.get("latest_idr_rates"));
    }

    @Test
    void shouldThrowAfterFreeze() {
        store.put("a", "b");
        store.markInitialized();

        assertThrows(IllegalStateException.class,
                () -> store.put("c", "d"));
    }

    @Test
    void shouldThrowForUnknownKey() {
        assertThrows(IllegalArgumentException.class,
                () -> store.get("unknown"));
    }
}
