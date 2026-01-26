package com.example.AlloBank.strategy;


import com.example.AlloBank.dto.LatestRateDto;
import com.example.AlloBank.response.LatestRatesResponse;
import com.example.AlloBank.store.FinanceStore;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LatestIdrRatesStrategyTest {

    @Test
    void shouldCalculateUsdBuySpread() {
        FinanceStore store = Mockito.mock(FinanceStore.class);

        LatestRatesResponse response = new LatestRatesResponse();
        response.setBase("IDR");
        response.setRates(Map.of("USD", 0.000065));

        Mockito.when(store.getLatestRates()).thenReturn(response);

        LatestIdrRatesStrategy strategy =
                new LatestIdrRatesStrategy(store, "githubuser");

        List<LatestRateDto> result = strategy.getData();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCurrency()).isEqualTo("USD");
        assertThat(result.get(0).getUsdBuySpreadIdr()).isGreaterThan(0);
    }

}
