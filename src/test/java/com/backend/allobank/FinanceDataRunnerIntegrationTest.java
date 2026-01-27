package com.backend.allobank;

import com.backend.allobank.runner.FinanceDataRunner;
import com.backend.allobank.store.InMemoryFinanceStore;
import com.backend.allobank.strategy.HistoricalIdrUsdStrategy;
import com.backend.allobank.strategy.IDRDataFetcherRegistry;
import com.backend.allobank.strategy.LatestIdrRatesStrategy;
import com.backend.allobank.strategy.SupportedCurrenciesStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = {
                FinanceDataRunnerIntegrationTest.TestConfig.class,
                FinanceDataRunner.class,
                InMemoryFinanceStore.class,
                IDRDataFetcherRegistry.class
        },
        properties = "spring.main.web-application-type=none"
)
class FinanceDataRunnerIntegrationTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        LatestIdrRatesStrategy latestIdrRatesStrategy() {
            return new LatestIdrRatesStrategy(null) {
                @Override
                public String getResourceType() {
                    return "latest_idr_rates";
                }

                @Override
                public Object fetchAndTransform() {
                    return Map.of("mock", "latest");
                }
            };
        }

        @Bean
        HistoricalIdrUsdStrategy historicalIdrUsdStrategy() {
            return new HistoricalIdrUsdStrategy(null) {
                @Override
                public String getResourceType() {
                    return "historical_idr_usd";
                }

                @Override
                public Object fetchAndTransform() {
                    return Map.of("mock", "historical");
                }
            };
        }

        @Bean
        SupportedCurrenciesStrategy supportedCurrenciesStrategy() {
            return new SupportedCurrenciesStrategy(null) {
                @Override
                public String getResourceType() {
                    return "supported_currencies";
                }

                @Override
                public Object fetchAndTransform() {
                    return Map.of("mock", "currencies");
                }
            };
        }
    }

    @Autowired
    private InMemoryFinanceStore store;

    @Test
    void shouldLoadAllFetcherDataIntoStoreOnApplicationStartup() {
        Map<String, Object> allData = store.getAll();

        assertEquals(3, allData.size());

        assertNotNull(allData.get("latest_idr_rates"));
        assertNotNull(allData.get("historical_idr_usd"));
        assertNotNull(allData.get("supported_currencies"));

        assertEquals(Map.of("mock", "latest"), allData.get("latest_idr_rates"));
        assertEquals(Map.of("mock", "historical"), allData.get("historical_idr_usd"));
        assertEquals(Map.of("mock", "currencies"), allData.get("supported_currencies"));
    }
}





