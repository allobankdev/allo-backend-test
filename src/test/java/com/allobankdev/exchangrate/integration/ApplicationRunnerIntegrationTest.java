package com.allobankdev.exchangrate.integration;

import com.allobankdev.exchangrate.constant.ResourceType;
import com.allobankdev.exchangrate.service.store.DataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ApplicationRunnerIntegrationTest {

    @Autowired
    private DataStore store;

    @Test
    public void shouldLoadAllDataBeforeAppReady() {
        assertNotNull(store.get(ResourceType.LATEST_RATES.getName()));
        assertNotNull(store.get(ResourceType.HISTORICAL_RATES.getName()));
        assertNotNull(store.get(ResourceType.SUPPORTED_CURRENCIES.getName()));
    }
}
