package com.allobank.finance.integration;

import com.allobank.finance.service.InMemoryFinanceStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DisplayName("ApplicationRunner Integration Tests")
class FinanceDataApplicationRunnerIntegrationTest {

    @Autowired
    private InMemoryFinanceStore store;

    @Test
    @DisplayName("Store harus sealed setelah ApplicationRunner selesai")
    void storeShouldBeSealedAfterStartup() {
        assertThat(store.isSealed()).isTrue();
    }

    @Test
    @DisplayName("Store tidak boleh menerima data baru setelah sealed")
    void storeShouldBeImmutableAfterSealing() {
        assertThat(store.isSealed()).isTrue();

        assertThatThrownBy(() ->
                store.put("any_key", List.of(Map.of("key", "value"))))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("sealed");
    }

    @Test
    @DisplayName("Data di store harus unmodifiable")
    void sealedStoreShouldReturnUnmodifiableLists() {
        store.getResourceTypes().forEach(resourceType ->
                store.get(resourceType).ifPresent(data ->
                        assertThatThrownBy(() ->
                                data.add(Map.of("unexpected", "write")))
                                .isInstanceOf(UnsupportedOperationException.class)
                )
        );
    }
}