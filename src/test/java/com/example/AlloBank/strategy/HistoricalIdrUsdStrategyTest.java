package com.example.AlloBank.strategy;

import com.example.AlloBank.dto.HistoricalRateDto;
import com.example.AlloBank.response.HistoricalRatesResponse;
import com.example.AlloBank.store.FinanceStore;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HistoricalIdrUsdStrategyTest {

    @Test
    void shouldReturnHistoricalRates() {
        FinanceStore store = Mockito.mock(FinanceStore.class);

        HistoricalRatesResponse response = new HistoricalRatesResponse();
        response.setBase("IDR");
        response.setRates(Map.of(
                "2024-01-01", Map.of("USD", 0.000065),
                "2024-01-02", Map.of("USD", 0.000066)
        ));

        Mockito.when(store.getHistoricalRates()).thenReturn(response);

        HistoricalIdrUsdStrategy strategy =
                new HistoricalIdrUsdStrategy(store);

        List<HistoricalRateDto> result = strategy.getData();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getRate()).isEqualTo(0.000065);
    }

}
