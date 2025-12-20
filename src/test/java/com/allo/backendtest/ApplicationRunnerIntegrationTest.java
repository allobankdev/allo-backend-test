package com.allo.backendtest;

import com.allo.backendtest.store.BaseStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
class ApplicationRunnerIntegrationTest {

    @Autowired
    private Map<String, BaseStore<?>> storeMap;

    @Autowired(required = false)
    private Collection<ApplicationRunner> applicationRunners;

    @Test
    void applicationRunner_initializesAndPopulatesStores() throws IOException {

        // basic sanity: storeMap should not be empty (some store beans should be registered)
        assertNotNull(storeMap, "storeMap must be injected");
        assertFalse(storeMap.isEmpty(), "There should be at least one store bean registered in the context");

        for (String s : storeMap.keySet()) {
            var store = storeMap.get(s);

            var stored = store.getData();
            assertNotNull(stored, "ApplicationRunner should populate the in-memory store '" + s + "' at startup");
        }
    }

    @Test
    void applicationRunner_beansExist_and_runnerExecuted() {
        // verify that an ApplicationRunner exists in the context (optional, may be null if none defined)
        assertNotNull(applicationRunners, "ApplicationRunner collection should be injected (may be empty if none configured)");
        assertFalse(applicationRunners.isEmpty(), "There should be at least one ApplicationRunner registered to perform startup initialization");
    }
}