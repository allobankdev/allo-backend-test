package com.bank.allo.store;

import com.bank.allo.repository.inbound.DataStore;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryDataStoreImplTest {

    @Test
    void testInitializeAndGet() {
        DataStore store = new InMemoryDataStoreImpl();

        store.initialize(Map.of("key", "value"));

        assertEquals("value", store.get("key"));
    }

    @Test
    void testInitializeOnlyOnce() {
        DataStore store = new InMemoryDataStoreImpl();

        store.initialize(Map.of("a", 1));

        assertThrows(
                IllegalStateException.class,
                () -> store.initialize(Map.of("b", 2)),
                "Expected second initialize() to throw"
        );
    }

    @Test
    void testGetUnknownKeyReturnsNull() {
        DataStore store = new InMemoryDataStoreImpl();

        store.initialize(Map.of("x", 99));

        assertNull(store.get("not_exist"));
    }
}
