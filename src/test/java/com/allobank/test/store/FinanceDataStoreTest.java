package com.allobank.test.store;

import org.junit.jupiter.api.Test;

import com.allobank.finance.exception.DataNotInitializedException;
import com.allobank.finance.exception.ResourceTypeNotSupportedException;
import com.allobank.finance.store.FinanceDataStore;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FinanceDataStoreTest {

    @Test
    void initializeOnceShouldKeepFirstValue() {
        FinanceDataStore store = new FinanceDataStore();

        store.initializeOnce(Map.of("latest_idr_rates", List.of(Map.of("value", "first"))));
        store.initializeOnce(Map.of("latest_idr_rates", List.of(Map.of("value", "second"))));

        assertEquals(List.of(Map.of("value", "first")), store.getByResourceType("latest_idr_rates"));
    }

    @Test
    void getByResourceTypeShouldThrowWhenStoreNotInitialized() {
        FinanceDataStore store = new FinanceDataStore();

        assertThrows(DataNotInitializedException.class, () -> store.getByResourceType("latest_idr_rates"));
    }

    @Test
    void getByResourceTypeShouldThrowForUnsupportedResourceType() {
        FinanceDataStore store = new FinanceDataStore();
        store.initializeOnce(Map.of("latest_idr_rates", List.of(Map.of("ok", true))));

        assertThrows(ResourceTypeNotSupportedException.class, () -> store.getByResourceType("not_found"));
    }

    @Test
    void storeValueShouldBeDeeplyImmutable() {
        FinanceDataStore store = new FinanceDataStore();
        store.initializeOnce(Map.of("latest_idr_rates", List.of(Map.of("base", "IDR"))));

        List<Map<String, Object>> storedValue = store.getByResourceType("latest_idr_rates");
        assertThrows(UnsupportedOperationException.class, () -> storedValue.add(Map.of()));
        assertThrows(UnsupportedOperationException.class, () -> storedValue.get(0).put("x", "y"));
    }
}
