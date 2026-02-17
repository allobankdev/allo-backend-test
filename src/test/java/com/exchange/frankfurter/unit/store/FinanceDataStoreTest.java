package com.exchange.frankfurter.unit.store;

import com.exchange.frankfurter.store.FinanceDataStore;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FinanceDataStoreTest {

    @Test
    void testStoreImmutability() {
        FinanceDataStore store = new FinanceDataStore();
        Map<String, Object> initialData = new HashMap<>();
        initialData.put("test_key", "original_value");

        store.initialize(initialData);

        // Pastikan data bisa diambil
        assertThat(store.get("test_key")).isEqualTo("original_value");

        // Pastikan Map yang dikembalikan getAll() tidak bisa dimodifikasi
        Map<String, Object> retrievedData = store.getAll();
        assertThrows(UnsupportedOperationException.class, () -> {
            retrievedData.put("new_key", "should_fail");
        });
    }
}
