package com.allobank.finance.service.fetcher.impl;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.SupportedCurrenciesDto;
import com.allobank.finance.enums.ResourceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesFetcherTest {

    @Mock
    private FrankfurterClient frankfurterClient;

    @InjectMocks
    private SupportedCurrenciesFetcher fetcher;

    @Test
    void fetchData_shouldReturnSupportedCurrencies() {
        SupportedCurrenciesDto mockDto = createMockSupportedCurrenciesDto();
        when(frankfurterClient.getSupportedCurrencies()).thenReturn(mockDto);

        SupportedCurrenciesDto result = fetcher.fetchData();

        assertSupportedCurrenciesDto(result);
    }

    @Test
    void fetchData_whenApiFails_shouldThrowException() {
        when(frankfurterClient.getSupportedCurrencies()).thenThrow(new RuntimeException("API Error"));

        assertThrows(RuntimeException.class, () -> fetcher.fetchData());
    }

    @Test
    void fetchData_whenReturnsNull_shouldHandleGracefully() {
        when(frankfurterClient.getSupportedCurrencies()).thenReturn(null);
        SupportedCurrenciesDto result = fetcher.fetchData();

        assertNull(result);
    }

    @Test
    void fetchData_whenReturnsEmptyMap_shouldReturnEmptyMap() {
        SupportedCurrenciesDto mockDto = new SupportedCurrenciesDto();
        when(frankfurterClient.getSupportedCurrencies()).thenReturn(mockDto);

        SupportedCurrenciesDto result = fetcher.fetchData();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getResourceType_shouldReturnCorrectValue() {
        assertEquals(ResourceType.SUPPORTED_CURRENCIES.getValue(), fetcher.getResourceType());
    }

    private SupportedCurrenciesDto createMockSupportedCurrenciesDto() {
        SupportedCurrenciesDto dto = new SupportedCurrenciesDto();
        dto.put("USD", "United States Dollar");
        dto.put("EUR", "Euro");
        dto.put("GBP", "British Pound");
        dto.put("JPY", "Japanese Yen");
        dto.put("IDR", "Indonesian Rupiah");
        return dto;
    }

    private void assertSupportedCurrenciesDto(SupportedCurrenciesDto result) {
        assertNotNull(result);
        assertEquals(5, result.size());

        assertEquals("United States Dollar", result.get("USD"));
        assertEquals("Euro", result.get("EUR"));
        assertEquals("British Pound", result.get("GBP"));
        assertEquals("Japanese Yen", result.get("JPY"));
        assertEquals("Indonesian Rupiah", result.get("IDR"));
    }
}