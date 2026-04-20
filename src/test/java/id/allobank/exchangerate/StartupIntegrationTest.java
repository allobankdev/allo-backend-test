package id.allobank.exchangerate;

import id.allobank.exchangerate.store.InMemoryDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StartupIntegrationTest {

    @Autowired
    InMemoryDataStore store;

    @Test
    void shouldLoadDataOnStartup() {
        assertNotNull(store.get(...));
    }
}