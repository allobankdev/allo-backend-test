package com.allo.backend;

import com.allo.backend.service.InMemoryDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "github.username=testuser",
        "frankfurter.api.base-url=https://api.frankfurter.app"
})
class AlloBackendTestApplicationTests {

    @Autowired
    private InMemoryDataStore dataStore;

    @Test
    void contextLoads() {
        assertThat(dataStore).isNotNull();
    }

    @Test
    void dataStoreIsInitializedOnStartup() {
        assertThat(dataStore.isInitialized()).isTrue();
    }

    @Test
    void allResourceTypesAreLoaded() {
        assertThat(dataStore.getAllData()).hasSize(3);
        assertThat(dataStore.getAllData()).containsKeys(
                "latest_idr_rates",
                "historical_idr_usd",
                "supported_currencies"
        );
    }
}
