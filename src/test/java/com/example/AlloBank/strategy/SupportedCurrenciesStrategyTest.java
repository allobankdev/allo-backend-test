package com.example.AlloBank.strategy;

import com.example.AlloBank.dto.CurrencyDto;
import com.example.AlloBank.response.CurrenciesResponse;
import com.example.AlloBank.store.FinanceStore;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SupportedCurrenciesStrategyTest {

    @Test
    void shouldReturnCurrencies() {
        FinanceStore store = Mockito.mock(FinanceStore.class);

        CurrenciesResponse response = new CurrenciesResponse();
        response.setCurrencies(Map.of(
                "USD", "United States Dollar",
                "EUR", "Euro"
        ));

        Mockito.when(store.getCurrencies()).thenReturn(response);

        SupportedCurrenciesStrategy strategy =
                new SupportedCurrenciesStrategy(store);

        List<CurrencyDto> result = strategy.getData();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCode()).isEqualTo("USD");
    }

}
