package com.allobank.finance;

import com.allobank.finance.cache.IDRDataFetcherCache;
import com.allobank.finance.model.FinanceData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "finance.api.base-url=https://api.frankfurter.app",
        "finance.github.username=manzoy"
})
class FinanceApiApplicationTest {

    @Autowired
    private IDRDataFetcherCache cache;

    @Test
    void contextLoads() {
        assertThat(cache).isNotNull();
    }

    @Test
    void shouldLoadAllResourceTypesOnStartup() {
        // Verify all three resource types are available after startup
        FinanceData latestRates = cache.get("latest_idr_rates");
        FinanceData historicalRates = cache.get("historical_idr_usd");
        FinanceData currencies = cache.get("supported_currencies");

        assertThat(latestRates).isNotNull();
        assertThat(historicalRates).isNotNull();
        assertThat(currencies).isNotNull();
    }
}
