package com.allo.backend.test.code.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataStorageServiceTest {

    private DataStorageService service;

    @BeforeEach
    void setUp() {
        service = new DataStorageService();
    }

    @Test
    void testStoreAndRetrieveData() {
        service.storeData("test_resource", "test_data");
        service.markAsInitialized();

        Object data = service.getData("test_resource");
        assertEquals("test_data", data);
    }

    @Test
    void testGetData_BeforeInitialization() {
        service.storeData("test_resource", "test_data");

        assertThrows(IllegalStateException.class,
                () -> service.getData("test_resource"));
    }

    @Test
    void testStoreData_AfterInitialization() {
        service.storeData("test_resource", "test_data");
        service.markAsInitialized();

        assertThrows(IllegalStateException.class,
                () -> service.storeData("another_resource", "another_data"));
    }

    @Test
    void testGetData_UnknownResource() {
        service.storeData("test_resource", "test_data");
        service.markAsInitialized();

        assertThrows(IllegalArgumentException.class,
                () -> service.getData("unknown_resource"));
    }

    @Test
    void testMarkAsInitialized_Twice() {
        service.storeData("test_resource", "test_data");
        service.markAsInitialized();

        assertThrows(IllegalStateException.class,
                () -> service.markAsInitialized());
    }

    @Test
    void testIsInitialized() {
        assertFalse(service.isInitialized());

        service.storeData("test_resource", "test_data");
        assertFalse(service.isInitialized());

        service.markAsInitialized();
        assertTrue(service.isInitialized());
    }
}
