package com.allobank.finance.service.fetcher.impl;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.HistoricalIdrUsdDto;
import com.allobank.finance.enums.ResourceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdFetcherTest {

    @Mock
    private FrankfurterClient frankfurterClient;

    @InjectMocks
    private HistoricalIdrUsdFetcher fetcher;

    private static final String START_DATE = "2024-01-01";
    private static final String END_DATE = "2024-01-05";
    private static final double USD_RATE_DAY1 = 15000.0;
    private static final double USD_RATE_DAY2 = 15100.0;

    @Test
    void fetchData_shouldReturnHistoricalData() {
        HistoricalIdrUsdDto mockDto = createMockHistoricalDto();
        when(frankfurterClient.getHistoricalRates()).thenReturn(mockDto);

        HistoricalIdrUsdDto result = fetcher.fetchData();

        assertHistoricalDto(result);
    }

    @Test
    void fetchData_whenApiFails_shouldThrowException() {
        when(frankfurterClient.getHistoricalRates()).thenThrow(new RestClientException("API Error"));

        assertThrows(RestClientException.class, () -> fetcher.fetchData());
    }

    @Test
    void fetchData_whenReturnsNull_shouldHandleGracefully() {
        when(frankfurterClient.getHistoricalRates()).thenReturn(null);

        HistoricalIdrUsdDto result = fetcher.fetchData();

        assertNull(result);
    }

    @Test
    void getResourceType_shouldReturnCorrectValue() {
        assertEquals(ResourceType.HISTORICAL_IDR_USD.getValue(), fetcher.getResourceType());
    }

    private HistoricalIdrUsdDto createMockHistoricalDto() {
        HistoricalIdrUsdDto dto = new HistoricalIdrUsdDto();
        dto.setAmount(1);
        dto.setBase("IDR");
        dto.setStartDate(START_DATE);
        dto.setEndDate(END_DATE);

        Map<String, Map<String, Double>> rates = Map.of(
                START_DATE, Map.of("USD", USD_RATE_DAY1),
                "2024-01-02", Map.of("USD", USD_RATE_DAY2),
                "2024-01-03", Map.of("USD", 14900.0),
                "2024-01-04", Map.of("USD", 15200.0),
                END_DATE, Map.of("USD", 15050.0)
        );

        dto.setRates(rates);
        return dto;
    }

    private void assertHistoricalDto(HistoricalIdrUsdDto result) {
        assertNotNull(result);
        assertEquals(1, result.getAmount());
        assertEquals("IDR", result.getBase());
        assertEquals(START_DATE, result.getStartDate());
        assertEquals(END_DATE, result.getEndDate());

        Map<String, Map<String, Double>> rates = result.getRates();
        assertNotNull(rates);
        assertEquals(5, rates.size());

        Map<String, Double> day1Rates = rates.get(START_DATE);
        assertNotNull(day1Rates);
        assertEquals(USD_RATE_DAY1, day1Rates.get("USD"));

        Map<String, Double> day2Rates = rates.get("2024-01-02");
        assertNotNull(day2Rates);
        assertEquals(USD_RATE_DAY2, day2Rates.get("USD"));
    }
}