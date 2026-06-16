package com.allobank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryDataStoreTest {

    private InMemoryDataStore dataStore;

    @BeforeEach
    void setUp() {
        dataStore = new InMemoryDataStore();
    }

    @Test
    void testStoreAndRetrieveData() {
        // Arrange
        String resourceType = "test_resource";
        String testData = "test_data";

        // Act
        dataStore.storeData(resourceType, testData);
        Object retrieved = dataStore.getData(resourceType);

        // Assert
        assertEquals(testData, retrieved);
    }

    @Test
    void testStoreData_NullData() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            dataStore.storeData("test", null);
        });
    }

    @Test
    void testMarkDataLoaded() {
        // Act
        dataStore.markDataLoaded();

        // Assert
        assertTrue(dataStore.isDataLoaded());
    }

    @Test
    void testStoreDataAfterMarkingLoaded() {
        // Arrange
        dataStore.markDataLoaded();

        // Act
        dataStore.storeData("test", "data");

        // Assert - should not throw, but should log warning
        assertNotNull(dataStore.getData("test"));
    }

    @Test
    void testGetData_NotFound() {
        // Act
        Object result = dataStore.getData("non_existent");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetAllData() {
        // Arrange
        dataStore.storeData("resource1", "data1");
        dataStore.storeData("resource2", "data2");

        // Act
        var allData = dataStore.getAllData();

        // Assert
        assertNotNull(allData);
        assertEquals(2, allData.size());
        assertTrue(allData.containsKey("resource1"));
        assertTrue(allData.containsKey("resource2"));
    }
}

