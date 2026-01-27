package allobankdev.test.finance.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import allobankdev.test.finance.store.FinanceDataStore;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class FinanceStartupIntegrationTest {

    @Autowired
    FinanceDataStore store;

    @Test
    void shouldLoadDataOnStartup() {
        Object latest = store.get("latest_idr_rates");
        Object historical = store.get("historical_idr_usd");
        Object currencies = store.get("supported_currencies");

        assertNotNull(latest);
        assertNotNull(historical);
        assertNotNull(currencies);
    }
}
