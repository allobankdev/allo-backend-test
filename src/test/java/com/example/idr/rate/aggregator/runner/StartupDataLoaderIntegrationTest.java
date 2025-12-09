package com.example.idr.rate.aggregator.runner;

import com.example.idr.rate.aggregator.store.ImmutableDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StartupDataLoaderIntegrationTest {

    @Autowired
    ImmutableDataStore store;

    @Test
    void storeIsLoaded() {
        assertNotNull(store);
    }
}
