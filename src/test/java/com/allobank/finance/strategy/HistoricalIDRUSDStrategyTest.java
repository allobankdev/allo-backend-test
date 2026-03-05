package com.allobank.finance.strategy;

import com.allobank.finance.config.FrankfurterProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("HistoricalIDRUSDStrategy Unit Tests")
class HistoricalIDRUSDStrategyTest {

    private HistoricalIDRUSDStrategy strategy;

    @BeforeEach
    void setUp() {
        FrankfurterProperties properties = new FrankfurterProperties();
        properties.getHistorical().setFrom("2024-01-01");
        properties.getHistorical().setTo("2024-01-05");
        strategy = new HistoricalIDRUSDStrategy(null, properties);
    }

    @Test
    @DisplayName("getResourceType() harus return 'historical_idr_usd'")
    void shouldReturnCorrectResourceType() {
        assertThat(strategy.getResourceType()).isEqualTo("historical_idr_usd");
    }

    @Test
    @DisplayName("Properties from harus '2024-01-01'")
    void shouldHaveCorrectFromDate() {
        FrankfurterProperties properties = new FrankfurterProperties();
        properties.getHistorical().setFrom("2024-01-01");
        assertThat(properties.getHistorical().getFrom()).isEqualTo("2024-01-01");
    }

    @Test
    @DisplayName("Properties to harus '2024-01-05'")
    void shouldHaveCorrectToDate() {
        FrankfurterProperties properties = new FrankfurterProperties();
        properties.getHistorical().setTo("2024-01-05");
        assertThat(properties.getHistorical().getTo()).isEqualTo("2024-01-05");
    }
}