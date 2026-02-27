package com.allobank.finance.service.fetcher.impl;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.LatestIdrRatesDto;
import com.allobank.finance.enums.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesFetcherTest {

    @Mock
    private FrankfurterClient frankfurterClient;

    @InjectMocks
    private LatestIdrRatesFetcher fetcher;

    private static final String GITHUB_USERNAME = "hosea-adrianus";
    private static final double USD_RATE = 15000.0;
    private static final double EXPECTED_SPREAD = 0.000066952;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fetcher, "githubUsername", GITHUB_USERNAME);
    }

    @Test
    void fetchData_shouldReturnDtoWithCalculatedSpread() {
        LatestIdrRatesDto mockDto = createMockDtoWithUsdRate();
        when(frankfurterClient.getLatestRates()).thenReturn(mockDto);

        LatestIdrRatesDto result = fetcher.fetchData();

        assertNotNull(result);
        assertEquals(USD_RATE, result.getRates().get("USD"));
        assertNotNull(result.getUsdBuySpreadIdr());
        assertEquals(EXPECTED_SPREAD, result.getUsdBuySpreadIdr(), 0.0000001);

        verify(frankfurterClient).getLatestRates();
    }

    @Test
    void fetchData_whenDtoIsNull_shouldReturnNull() {
        when(frankfurterClient.getLatestRates()).thenReturn(null);

        LatestIdrRatesDto result = fetcher.fetchData();

        assertNull(result);
        verify(frankfurterClient).getLatestRates();
    }

    @Test
    void fetchData_whenRatesIsNull_shouldReturnDtoWithoutSpread() {
        LatestIdrRatesDto dto = new LatestIdrRatesDto();
        dto.setRates(null);

        when(frankfurterClient.getLatestRates()).thenReturn(dto);

        LatestIdrRatesDto result = fetcher.fetchData();

        assertNotNull(result);
        assertNull(result.getUsdBuySpreadIdr());

        verify(frankfurterClient).getLatestRates();
    }

    @Test
    void fetchData_whenUsdRateMissing_shouldReturnDtoWithoutSpread() {
        LatestIdrRatesDto dto = new LatestIdrRatesDto();
        dto.setRates(Map.of("EUR", 16000.0));

        when(frankfurterClient.getLatestRates()).thenReturn(dto);

        LatestIdrRatesDto result = fetcher.fetchData();

        assertNotNull(result);
        assertNull(result.getUsdBuySpreadIdr());

        verify(frankfurterClient).getLatestRates();
    }

    @Test
    void fetchData_whenApiFails_shouldThrowException() {
        when(frankfurterClient.getLatestRates())
                .thenThrow(new RestClientException("API Error"));

        assertThrows(RestClientException.class, () -> fetcher.fetchData());

        verify(frankfurterClient).getLatestRates();
    }

    @Test
    void getResourceType_shouldReturnCorrectValue() {
        assertEquals(ResourceType.LATEST_IDR_RATES.getValue(), fetcher.getResourceType());
    }

    private LatestIdrRatesDto createMockDtoWithUsdRate() {
        LatestIdrRatesDto dto = new LatestIdrRatesDto();
        dto.setRates(Map.of("USD", USD_RATE));
        return dto;
    }
}