package com.allobank.runner;

import com.allobank.store.DataStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(properties = {
        "app.github-username=testuser123",
        "frankfurter.api.base-url=https://api.frankfurter.app"
})
class DataInitializationRunnerTest {

    @Autowired
    private DataStoreService dataStoreService;

    @Test
    void testDataStoreInitializedOnStartup() {
        // Verify data store is initialized
        assertThat(dataStoreService.isInitialized()).isTrue();

        // Verify all three resources are loaded
        assertThat(dataStoreService.getAllData()).hasSize(3);

        // Verify each resource type is accessible
        assertThatCode(() -> dataStoreService.getData("latest_idr_rates"))
                .doesNotThrowAnyException();

        assertThatCode(() -> dataStoreService.getData("historical_idr_usd"))
                .doesNotThrowAnyException();

        assertThatCode(() -> dataStoreService.getData("supported_currencies"))
                .doesNotThrowAnyException();
    }

    @Test
    void testDataStoreIsImmutable() {
        // Verify attempting to modify after initialization throws exception
        assertThatThrownBy(() ->
                dataStoreService.storeData(
                        com.allobank.enums.ResourceType.LATEST_IDR_RATES,
                        new Object()
                )
        ).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already initialized");
    }
}