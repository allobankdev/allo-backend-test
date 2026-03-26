package com.allobank.test.store;

import com.allobank.test.exception.DataNotInitializedException;
import com.allobank.test.exception.ResourceTypeNotSupportedException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FinanceDataStoreTest {

    @Test
    void initializeOnceShouldKeepFirstValue() {
        FinanceDataStore store = new FinanceDataStore();

        store.initializeOnce(Map.of("latest_idr_rates", "first"));
        store.initializeOnce(Map.of("latest_idr_rates", "second"));

        assertEquals("first", store.getByResourceType("latest_idr_rates"));
    }

    @Test
    void getByResourceTypeShouldThrowWhenStoreNotInitialized() {
        FinanceDataStore store = new FinanceDataStore();

        assertThrows(DataNotInitializedException.class, () -> store.getByResourceType("latest_idr_rates"));
    }

    @Test
    void getByResourceTypeShouldThrowForUnsupportedResourceType() {
        FinanceDataStore store = new FinanceDataStore();
        store.initializeOnce(Map.of("latest_idr_rates", Map.of("ok", true)));

        assertThrows(ResourceTypeNotSupportedException.class, () -> store.getByResourceType("not_found"));
    }
}
