package com.allo.bank.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.allo.bank.client.FrankfurterClient;
import com.allo.bank.client.dto.FrankfurterLatestResponse;
import com.allo.bank.dto.FinanceDataItem;
import com.allo.bank.util.SpreadFactorCalculator;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesFetcherTest {

    @Mock
    private FrankfurterClient frankfurterClient;

    @Mock
    private SpreadFactorCalculator spreadFactorCalculator;

    @InjectMocks
    private LatestIdrRatesFetcher fetcher;

    @Test
    void shouldTransformLatestRatesAndAddSpreadValue() {
        FrankfurterLatestResponse response = new FrankfurterLatestResponse();
        response.setBase("IDR");
        response.setDate("2024-01-05");
        response.setAmount(1D);
        response.setRates(Map.of("USD", 0.000064D, "EUR", 0.000059D));

        when(frankfurterClient.fetchLatestIdrRates()).thenReturn(response);
        when(spreadFactorCalculator.calculateSpreadFactor()).thenReturn(new BigDecimal("0.00765"));

        FinanceDataItem item = fetcher.fetch().get(0);

        assertThat(item.resourceType()).isEqualTo(LatestIdrRatesFetcher.RESOURCE_TYPE);
        assertThat(item.payload()).containsEntry("base", "IDR");
        assertThat(item.payload()).containsEntry("spreadFactor", new BigDecimal("0.00765"));
        assertThat(item.payload()).containsKey("USD_BuySpread_IDR");
    }
}
