package com.allobank.finance.service.strategy;

import com.allobank.finance.integration.frankfurter.FrankfurterClient;
import com.allobank.finance.integration.frankfurter.dto.LatestRatesRawResponseBuilder;
import com.allobank.finance.model.response.LatestRatesResponse;
import com.allobank.finance.util.SpreadCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class LatestIDRRatesFetcherTest {

    @Mock
    FrankfurterClient client;

    @Mock
    SpreadCalculator spreadCalculator;

    @InjectMocks
    LatestIDRRatesFetcher fetcher;

    @Test
    void shouldMapLatestRatesWithSpreadAndDynamicField() {
        var rates = new LinkedHashMap<String, Double>();
        rates.put("USD", 0.000064);
        rates.put("AUD", 0.000084);

        var raw = LatestRatesRawResponseBuilder.builder()
                .base("IDR")
                .date("2024-01-01")
                .rates(rates)
                .build();

        Mockito.when(client.fetchLatestRates()).thenReturn(raw);
        Mockito.when(spreadCalculator.calculateSpreadFactor()).thenReturn(0.005); // 0.5%

        var result = (List<LatestRatesResponse>) fetcher.fetchData();

        Assertions.assertEquals(2, result.size());

        var aud = result.get(0);
        Assertions.assertEquals("AUD", aud.currency());
        Assertions.assertEquals(0.000084, aud.rate());

        var audDynamic = aud.dynamicFields();
        Assertions.assertTrue(audDynamic.containsKey("aud_buy_spread_idr"));

        double expectedAudSpread = (1 / 0.000084) * (1 + 0.005);
        Assertions.assertEquals(expectedAudSpread,
                (double) audDynamic.get("aud_buy_spread_idr"));

        var usd = result.get(1);
        Assertions.assertEquals("USD", usd.currency());

        var usdDynamic = usd.dynamicFields();
        Assertions.assertTrue(usdDynamic.containsKey("usd_buy_spread_idr"));

        double expectedUsdSpread = (1 / 0.000064) * (1 + 0.005);
        Assertions.assertEquals(expectedUsdSpread,
                (double) usdDynamic.get("usd_buy_spread_idr"));

        Mockito.verify(client).fetchLatestRates();
        Mockito.verify(spreadCalculator).calculateSpreadFactor();
    }
}
