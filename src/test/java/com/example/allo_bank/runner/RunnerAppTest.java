package com.example.allo_bank.runner;

import com.example.allo_bank.integration.dto.HistoricalIdrUsdDto;
import com.example.allo_bank.integration.dto.LatestIdrRatesDto;
import com.example.allo_bank.service.GetIntegrationDataService;
import com.example.allo_bank.util.Cache;
import com.example.allo_bank.util.TypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class RunnerAppTest {

    @Autowired
    private Cache cache;

    @MockitoBean
    private GetIntegrationDataService getIntegrationDataService;

    @Test
    void shouldLoadCacheAfterApplicationStart() {
        // Given - dummy data for mocking
        LatestIdrRatesDto latestMock = new LatestIdrRatesDto();
        HistoricalIdrUsdDto historicalMock = new HistoricalIdrUsdDto();
        Map<String, String> currencyMock = Map.of("USD", "United States Dollar");

        // Mock external service calls
        when(getIntegrationDataService.getLatestIdrRates()).thenReturn(latestMock);
        when(getIntegrationDataService.getHistoricalIdrUsd()).thenReturn(historicalMock);
        when(getIntegrationDataService.getSupportedCurrencies()).thenReturn(currencyMock);

        // When
        // ApplicationRunner is automatically executed at @SpringBootTest startup

        // Then - cache must be marked ready
        assertThat(cache.isReady()).isTrue();

        // Cache must contain exactly 3 entries
        Map<String, Object> cached = cache.getAllCache();
        assertThat(cached).hasSize(3);

    }
}
