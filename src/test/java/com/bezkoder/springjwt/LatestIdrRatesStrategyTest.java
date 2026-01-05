package com.bezkoder.springjwt;

import com.bezkoder.springjwt.client.FrankfurterApiClient;
import com.bezkoder.springjwt.config.GitHubProperties;
import com.bezkoder.springjwt.store.FinanceDataStore;
import com.bezkoder.springjwt.strategy.LatestIdrRatesStrategy;
import com.bezkoder.springjwt.util.SpreadCalculator;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LatestIdrRatesStrategyTest {
    @Test
    void shouldAddUsdBuySpreadIdrField() {
        FrankfurterApiClient api = mock(FrankfurterApiClient.class);
        FinanceDataStore store = new FinanceDataStore();

        GitHubProperties props = new GitHubProperties();
        props.setUsername("johndoe47");
        SpreadCalculator calc = new SpreadCalculator(props);

        Map<String, Object> response = new HashMap<>();
        response.put("base", "IDR");
        response.put("date", "2026-01-04");
        response.put("rates", new HashMap<>(Map.of(
            "USD", 0.000064,
            "EUR", 0.000058
        )));

        when(api.getLatestBaseIdr()).thenReturn(response);

        LatestIdrRatesStrategy s =
            new LatestIdrRatesStrategy(api, store, calc);

        s.loadAtStartup();

        Map<String, Object> obj =
            (Map<String, Object>) s.loadedData().get(0);

        assertTrue(obj.containsKey("USD_BuySpread_IDR"));
    }

}
