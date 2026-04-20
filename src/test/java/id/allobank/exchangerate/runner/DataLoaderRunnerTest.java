package id.allobank.exchangerate.runner;

import id.allobank.exchangerate.store.InMemoryDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DataLoaderRunnerTest {

    @Autowired
    private InMemoryDataStore store;

    @Test
    void testDataLoaded() {
        assertNotNull(store.get("latest_idr_rates"));
        assertNotNull(store.get("historical_idr_usd"));
        assertNotNull(store.get("supported_currencies"));
    }
}