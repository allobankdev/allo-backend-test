package com.bank.allo;

import com.bank.allo.repository.inbound.DataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationStartupIntegrationTest {

    @Autowired
    private DataStore dataStore;

    @Test
    void runner_should_load_all_resources_on_startup() {
        assertNotNull(dataStore.get("latest_idr_rates"), "latest_idr_rates must be loaded");
        assertNotNull(dataStore.get("historical_idr_usd"), "historical_idr_usd must be loaded");
        assertNotNull(dataStore.get("supported_currencies"), "supported_currencies must be loaded");

        Object latest = dataStore.get("latest_idr_rates");
        if (latest instanceof java.util.Map<?,?> map) {
            assertThrows(UnsupportedOperationException.class, () -> {
                ((java.util.Map) map).put("X", "Y");
            });
        }
    }
}
