package com.allo.finance.integration;

import com.allo.finance.service.FinanceDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FinanceIntegrationTest {

    @Autowired
    private FinanceDataStore store;

    @Test
    void shouldLoadAllFinanceData() {
        Map<String, Object> snapshot = store.snapshot();

        assertThat(snapshot).containsKeys(
            "latest_idr_rates",
            "historical_idr_usd",
            "supported_currencies"
        );

        Map<String, Object> latest = (Map<String, Object>) snapshot.get("latest_idr_rates");
        assertThat(latest).containsKey("rates");
        assertThat(latest).containsKey("USD_BuySpread_IDR");

        Map<String, Object> historical = (Map<String, Object>) snapshot.get("historical_idr_usd");
        assertThat(historical).containsKeys("start_date", "end_date", "rates");

        Map<String, Object> supported = (Map<String, Object>) snapshot.get("supported_currencies");
        assertThat(supported).containsKey("USD");
        assertThat(supported).containsKey("IDR");

        double usdBuySpread = ((Number)((Map<String, Object>)latest.get("rates")).get("USD")).doubleValue();
        assertThat(latest.get("USD_BuySpread_IDR")).isNotNull();
        assertThat((Double) latest.get("USD_BuySpread_IDR")).isGreaterThan(usdBuySpread);

        Map<String, Object> secondAccess = store.snapshot();
        assertThat(secondAccess).isEqualTo(snapshot);
    }

}