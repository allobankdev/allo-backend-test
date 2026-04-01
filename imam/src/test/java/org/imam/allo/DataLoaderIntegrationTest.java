package org.imam.allo;

import org.imam.allo.service.DataStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class DataLoaderIntegrationTest {

    @Autowired
    private DataStoreService store;

    @Test
    void shouldLoadAllDataOnStartup() {
        assertNotNull(store.get("latest_idr_rates"));
        assertNotNull(store.get("supported_currencies"));
        assertNotNull(store.get("historical_idr_usd"));
    }

}
