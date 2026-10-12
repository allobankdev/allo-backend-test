package com.allobank.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataStoreTest {
    
    @Autowired
    private DataStore dataStore;
    
    @Test
    void testStoreAndRetrieveData() {
        // Arrange
        String resourceType = "test_resource";
        String testData = "test_data";
        
        // Act
        boolean stored = dataStore.storeData(resourceType, testData);
        Object retrieved = dataStore.getData(resourceType);
        
        // Assert
        assertTrue(stored);
        assertEquals(testData, retrieved);
    }
    
    @Test
    void testGetNonExistentResourceReturnsNull() {
        // Act
        Object result = dataStore.getData("non_existent");
        
        // Assert
        assertNull(result);
    }
    
    @Test
    void testThreadSafetyWithMultipleConcurrentReads() throws InterruptedException {
        // Arrange
        String resourceType = "concurrent_test";
        String testData = "test_value";
        dataStore.storeData(resourceType, testData);
        
        // Act: Create multiple threads to read concurrently
        Thread[] threads = new Thread[10];
        Object[] results = new Object[10];
        
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = dataStore.getData(resourceType);
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Assert: All threads should retrieve the same data
        for (Object result : results) {
            assertEquals(testData, result);
        }
    }
    
    @Test
    void testInitializationBlock() {
        // Create fresh instance for this test
        DataStore freshStore = new DataStore();
        
        // Initially not initialized
        assertFalse(freshStore.isInitialized());
        
        // Mark as initialized
        freshStore.markAsInitialized();
        
        // Should now be initialized
        assertTrue(freshStore.isInitialized());
        
        // Attempting to store after initialization should fail
        boolean result = freshStore.storeData("test", "data");
        assertFalse(result);
    }
    
    @Test
    void testGetAllDataReturnsImmutableMap() {
        // Arrange
        String resource1 = "resource1";
        String resource2 = "resource2";
        dataStore.storeData(resource1, "data1");
        dataStore.storeData(resource2, "data2");
        
        // Act
        var allData = dataStore.getAllData();
        
        // Assert
        assertNotNull(allData);
        assertEquals(2, allData.size());
        
        // Attempt to modify returned map should throw exception
        assertThrows(UnsupportedOperationException.class, () -> {
            allData.put("new_key", "new_value");
        });
    }
}
