package com.allo_backend_test.strategy;

import com.allo_backend_test.client.FrankfurterApiClient;
import com.allo_backend_test.dto.FinanceResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SupportedCurrenciesStrategyTest {

    @Test
    void shouldReturnSupportedCurrencies() {

        FrankfurterApiClient client = Mockito.mock(FrankfurterApiClient.class);

        Mockito.when(client.fetchSupportedCurrencies())
                .thenReturn(Map.of(
                        "USD", "United States Dollar",
                        "IDR", "Indonesian Rupiah"
                ));

        SupportedCurrenciesStrategy strategy =
                new SupportedCurrenciesStrategy(client);

        List<FinanceResponseDto> result = strategy.fetch();

        assertThat(result).hasSize(2);

        assertThat(result)
                .extracting(FinanceResponseDto::getKey)
                .containsExactlyInAnyOrder("USD", "IDR");

        assertThat(result)
                .extracting(FinanceResponseDto::getValue)
                .containsExactlyInAnyOrder("United States Dollar", "Indonesian Rupiah");
    }

}
