package com.allobank.test.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.strategy.SupportedCurrenciesFetcher;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesFetcherTest {

    @Mock
    private FrankfurterClient frankfurterClient;

    @InjectMocks
    private SupportedCurrenciesFetcher fetcher;

    @Test
    void resourceTypeShouldMatchContract() {
        assertEquals("supported_currencies", fetcher.resourceType());
    }

    @Test
    void fetchShouldReturnSortedUnifiedCurrencyRows() {
        when(frankfurterClient.fetchSupportedCurrenciesRaw()).thenReturn(Map.of(
                "USD", "United States Dollar",
                "IDR", "Indonesian Rupiah"));

        List<Map<String, Object>> actual = fetcher.fetch();

        assertEquals(2, actual.size());
        assertEquals("IDR", actual.get(0).get("code"));
        assertEquals("Indonesian Rupiah", actual.get(0).get("name"));
        assertEquals("USD", actual.get(1).get("code"));
        assertEquals("United States Dollar", actual.get(1).get("name"));
        assertEquals("supported_currencies", actual.get(0).get("resourceType"));
    }
}
