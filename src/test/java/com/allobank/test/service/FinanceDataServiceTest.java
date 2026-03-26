package com.allobank.test.service;

import com.allobank.test.store.FinanceDataStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinanceDataServiceTest {

    @Mock
    private FinanceDataStore financeDataStore;

    @InjectMocks
    private FinanceDataService financeDataService;

    @Test
    void findByResourceTypeShouldDelegateToStore() {
        Map<String, Object> expected = Map.of("base", "EUR");
        when(financeDataStore.getByResourceType("latest_idr_rates")).thenReturn(expected);

        Object actual = financeDataService.findByResourceType("latest_idr_rates");

        assertEquals(expected, actual);
    }

    @Test
    void supportedResourceTypesShouldDelegateToStore() {
        List<String> expected = List.of("latest_idr_rates", "historical_idr_usd", "supported_currencies");
        when(financeDataStore.supportedResourceTypes()).thenReturn(expected);

        List<String> actual = financeDataService.supportedResourceTypes();

        assertEquals(expected, actual);
    }
}
