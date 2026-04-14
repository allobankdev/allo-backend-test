package com.allobank.test.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.allobank.finance.exception.ResourceTypeNotSupportedException;
import com.allobank.finance.service.FinanceDataService;
import com.allobank.finance.store.FinanceDataStore;
import com.allobank.finance.strategy.IDRDataFetcher;
import com.allobank.finance.strategy.IDRDataFetcherRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinanceDataServiceTest {

    @Mock
    private FinanceDataStore financeDataStore;

    @Mock
    private IDRDataFetcherRegistry registry;

    @InjectMocks
    private FinanceDataService financeDataService;

    @Test
    void findByResourceTypeShouldDelegateToStore() {
        List<Map<String, Object>> expected = List.of(Map.of("base", "IDR"));
        when(registry.asMap()).thenReturn(Map.of("latest_idr_rates", mock(IDRDataFetcher.class)));
        when(financeDataStore.getByResourceType("latest_idr_rates")).thenReturn(expected);

        List<Map<String, Object>> actual = financeDataService.findByResourceType("latest_idr_rates");

        assertEquals(expected, actual);
    }

    @Test
    void findByResourceTypeShouldNormalizeIncomingValue() {
        List<Map<String, Object>> expected = List.of(Map.of("base", "IDR"));
        when(registry.asMap()).thenReturn(Map.of("latest_idr_rates", mock(IDRDataFetcher.class)));
        when(financeDataStore.getByResourceType("latest_idr_rates")).thenReturn(expected);

        List<Map<String, Object>> actual = financeDataService.findByResourceType("  LATEST_IDR_RATES ");

        assertEquals(expected, actual);
    }

    @Test
    void supportedResourceTypesShouldUseRegistryKeys() {
        List<String> expected = List.of("latest_idr_rates", "historical_idr_usd", "supported_currencies");
        when(registry.asMap()).thenReturn(Map.of(
                "latest_idr_rates", mock(IDRDataFetcher.class),
                "historical_idr_usd", mock(IDRDataFetcher.class),
                "supported_currencies", mock(IDRDataFetcher.class)));

        List<String> actual = financeDataService.supportedResourceTypes();

        List<String> sortedExpected = new ArrayList<>(expected);
        List<String> sortedActual = new ArrayList<>(actual);
        sortedExpected.sort(String::compareTo);
        sortedActual.sort(String::compareTo);
        assertEquals(sortedExpected, sortedActual);
    }

    @Test
    void findByResourceTypeShouldThrowWhenResourceTypeUnsupported() {
        when(registry.asMap()).thenReturn(Map.of());

        assertThrows(ResourceTypeNotSupportedException.class, () -> financeDataService.findByResourceType("unknown"));
    }
}
