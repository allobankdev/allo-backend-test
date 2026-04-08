package com.example.finance.runner;

import com.example.finance.storage.InMemoryDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataLoaderRunnerTest {

    @Autowired
    private InMemoryDataStore store;

    @Test
    void shouldLoadAllDataOnStartup() {

        Map<String, Object> data = store.getAllData();

        assertNotNull(data);

        assertNotNull(data.get("latest_idr_rates"));
        assertNotNull(data.get("historical_idr_usd"));
        assertNotNull(data.get("supported_currencies"));
    }
}