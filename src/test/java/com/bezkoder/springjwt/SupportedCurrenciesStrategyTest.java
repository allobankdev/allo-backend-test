package com.bezkoder.springjwt;

import com.bezkoder.springjwt.client.FrankfurterApiClient;
import com.bezkoder.springjwt.store.FinanceDataStore;
import com.bezkoder.springjwt.strategy.SupportedCurrenciesStrategy;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupportedCurrenciesStrategyTest {

    @Test
    void shouldTransformCurrenciesToArray() {
        FrankfurterApiClient api = mock(FrankfurterApiClient.class);
        FinanceDataStore store = new FinanceDataStore();

        when(api.getCurrencies()).thenReturn(Map.of(
                "USD", "United States Dollar",
                "IDR", "Indonesian Rupiah"
        ));

        SupportedCurrenciesStrategy s = new SupportedCurrenciesStrategy(api, store);
        s.loadAtStartup();

        assertEquals(2, s.loadedData().size());
        Map<String, Object> item = (Map<String, Object>) s.loadedData().get(0);
        assertTrue(item.containsKey("code"));
        assertTrue(item.containsKey("name"));
    }
}
