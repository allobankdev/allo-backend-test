package com.allo.bank.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.allo.bank.client.FrankfurterClient;
import com.allo.bank.dto.FinanceDataItem;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesFetcherTest {

    @Mock
    private FrankfurterClient frankfurterClient;

    @InjectMocks
    private SupportedCurrenciesFetcher fetcher;

    @Test
    void shouldReturnCurrenciesAsSinglePayload() {
        when(frankfurterClient.fetchSupportedCurrencies()).thenReturn(Map.of("USD", "US Dollar", "IDR", "Indonesian Rupiah"));

        FinanceDataItem item = fetcher.fetch().get(0);

        assertThat(item.resourceType()).isEqualTo(SupportedCurrenciesFetcher.RESOURCE_TYPE);
        assertThat(item.payload()).containsEntry("USD", "US Dollar");
        assertThat(item.payload()).containsEntry("IDR", "Indonesian Rupiah");
    }
}
