package com.allobank.allobanktest.runner;

import com.allobank.allobanktest.store.FinanceDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class FinanceDataLoaderTest {

    @Autowired
    private FinanceDataStore store;

    @Test
    void shouldInitializeFinanceDataDuringStartup() {
        // supported_currencies MUST exist
        Object currencies = store.get("supported_currencies");

        assertThat(currencies).isNotNull();
    }

    @Test
    void storeShouldBeImmutableAfterStartup() {
        assertThatThrownBy(() ->
                store.put("test", "value")
        ).isInstanceOf(IllegalStateException.class);
    }

}
