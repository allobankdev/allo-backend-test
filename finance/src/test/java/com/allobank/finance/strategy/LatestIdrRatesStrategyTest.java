package com.allobank.finance.strategy;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.config.AppProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesStrategyTest {

    @Mock
    private FrankfurterClient client;

    @Mock
    private AppProperties appProperties;

    @InjectMocks
    private LatestIdrRatesStrategy strategy;

    @Test
    void shouldCalculateUsdBuySpreadIdrCorrectly() {
       
        when(appProperties.getGithubUsername()).thenReturn("hizkiarenat");

        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("rates", Map.of("USD", 0.000064));

        when(client.getLatestIdrRates()).thenReturn(apiResponse);

        Object resultObj = strategy.fetch();

        assertNotNull(resultObj);
        assertTrue(resultObj instanceof Map);

        Map<String, Object> result = (Map<String, Object>) resultObj;

        assertTrue(result.containsKey("USD_BuySpread_IDR"));
        assertTrue(result.containsKey("spreadFactor"));

        double spreadFactor = (double) result.get("spreadFactor");
        double usdBuySpreadIdr = (double) result.get("USD_BuySpread_IDR");

        assertTrue(spreadFactor > 0);
        assertTrue(usdBuySpreadIdr > 0);
    }
}
