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
import com.allo.bank.client.dto.FrankfurterHistoricalResponse;
import com.allo.bank.dto.FinanceDataItem;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdFetcherTest {

    @Mock
    private FrankfurterClient frankfurterClient;

    @InjectMocks
    private HistoricalIdrUsdFetcher fetcher;

    @Test
    void shouldTransformHistoricalRates() {
        FrankfurterHistoricalResponse response = new FrankfurterHistoricalResponse();
        response.setBase("IDR");
        response.setAmount(1D);
        response.setRates(Map.of("2024-01-01", Map.of("USD", 0.000064D)));

        when(frankfurterClient.fetchHistoricalIdrUsd()).thenReturn(response);

        FinanceDataItem item = fetcher.fetch().get(0);

        assertThat(item.resourceType()).isEqualTo(HistoricalIdrUsdFetcher.RESOURCE_TYPE);
        assertThat(item.payload()).containsEntry("base", "IDR");
        assertThat(item.payload()).containsKey("rates");
    }
}
