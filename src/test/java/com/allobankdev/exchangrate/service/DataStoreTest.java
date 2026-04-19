package com.allobankdev.exchangrate.service;

import com.allobankdev.exchangrate.constant.ResourceType;
import com.allobankdev.exchangrate.dto.LatestRateResponse;
import com.allobankdev.exchangrate.execption.NotFoundException;
import com.allobankdev.exchangrate.service.store.DataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataStoreTest {
    private DataStore store;

    @BeforeEach
    void setup() {
        store = new DataStore();
    }

    @Test
    public void testSaveAndGet() {
        ResourceType key = ResourceType.LATEST_RATES;
        LatestRateResponse value = new LatestRateResponse();

        store.save(key, value);
        Object retrieved = store.get(key.getName());

        assertEquals(value, retrieved);
    }

    @Test
    public void testGetNotFound() {
        assertThrows(NotFoundException.class, () -> {
            store.get("Invalid_type").toString();
        });
    }
}
