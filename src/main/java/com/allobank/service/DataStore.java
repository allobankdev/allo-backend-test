package com.allobank.service;

import com.allobank.dto.CurrenciesResponse;
import com.allobank.dto.HistoricalRatesResponse;
import com.allobank.dto.LatestRatesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe, immutable in-memory data store for caching fetched data
 * from the Frankfurter API.
 * 
 * Uses a ReadWriteLock to allow concurrent reads while ensuring
 * thread-safe writes during application startup.
 */
@Slf4j
@Service
public class DataStore {
    
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<String, Object> store = new HashMap<>();
    private volatile boolean initialized = false;
    
    /**
     * Store data for a resource type.
     * This should only be called during application startup.
     * After initialization, this method returns false to prevent overwriting data.
     * 
     * @param resourceType the resource type identifier
     * @param data the data to store
     * @return true if stored successfully, false if already initialized
     */
    public boolean storeData(String resourceType, Object data) {
        if (initialized) {
            log.warn("Attempted to store data after initialization is complete");
            return false;
        }
        
        lock.writeLock().lock();
        try {
            store.put(resourceType, data);
            log.debug("Stored data for resource type: {}", resourceType);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Retrieve data for a specific resource type.
     * Returns null if the resource type is not found.
     * 
     * @param resourceType the resource type identifier
     * @return the stored data, or null if not found
     */
    public Object getData(String resourceType) {
        lock.readLock().lock();
        try {
            return store.get(resourceType);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Mark the store as fully initialized.
     * After this call, no more data can be stored.
     */
    public void markAsInitialized() {
        lock.writeLock().lock();
        try {
            initialized = true;
            log.info("DataStore marked as initialized. Total resources stored: {}", store.size());
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Check if the store has been fully initialized.
     * 
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Get all stored data as an immutable map.
     * 
     * @return immutable copy of the stored data
     */
    public Map<String, Object> getAllData() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableMap(new HashMap<>(store));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Get the number of stored resources.
     * 
     * @return number of resources
     */
    public int getResourceCount() {
        lock.readLock().lock();
        try {
            return store.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}
