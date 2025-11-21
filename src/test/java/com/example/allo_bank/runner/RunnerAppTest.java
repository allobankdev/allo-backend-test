package com.example.allo_bank.runner;

import com.example.allo_bank.integration.dto.HistoricalIdrUsdDto;
import com.example.allo_bank.integration.dto.LatestIdrRatesDto;
import com.example.allo_bank.service.GetIntegrationDataService;
import com.example.allo_bank.util.Cache;
import com.example.allo_bank.util.TypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.example.allo_bank.util.Constant.IDR;
import static com.example.allo_bank.util.Constant.USD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RunnerAppTest {

    @Autowired
    private RunnerApp runnerApp;

    @Autowired
    private Cache cache;

    @MockitoBean
    private GetIntegrationDataService getIntegrationDataService;

    @MockitoBean
    private ApplicationArguments applicationArguments;

    @Test
    void testRunnerPopulatesCacheOnStartup() throws Exception {
        // Arrange: siapkan dummy data
        LatestIdrRatesDto latestDto = new LatestIdrRatesDto();
        latestDto.setBase(IDR);
        latestDto.setDate("2025-11-21");
        latestDto.setRates(Map.of(USD, BigDecimal.valueOf(15000)));

        HistoricalIdrUsdDto historicalDto = new HistoricalIdrUsdDto();
        Map<String, Map<String, BigDecimal>> rates = new HashMap<>();
        rates.put(USD, Map.of(IDR, BigDecimal.valueOf(15000)));
        historicalDto.setRates(rates);

        Map<String, String> supportedCurrencies = Map.of(USD, "US Dollar", IDR, "Rupiah");

        // Stub service calls
        when(getIntegrationDataService.getLatestIdrRates()).thenReturn(latestDto);
        when(getIntegrationDataService.getHistoricalIdrUsd()).thenReturn(historicalDto);
        when(getIntegrationDataService.getSupportedCurrencies()).thenReturn(supportedCurrencies);

        // Act: panggil runner
        runnerApp.run(applicationArguments);

        // Assert: cache terisi
        assertTrue(cache.isReady(), "Cache should be ready");

        LatestIdrRatesDto cachedLatest = cache.getDataCache(TypeEnum.latest_idr_rates);
        assertEquals(latestDto, cachedLatest, "LatestIdrRatesDto should be cached");

        HistoricalIdrUsdDto cachedHistorical = cache.getDataCache(TypeEnum.historical_idr_usd);
        assertEquals(historicalDto, cachedHistorical, "HistoricalIdrUsdDto should be cached");

        Map<String, String> cachedCurrencies = cache.getDataCache(TypeEnum.supported_currencies);
        assertEquals(supportedCurrencies, cachedCurrencies, "Supported currencies should be cached");
    }

}
