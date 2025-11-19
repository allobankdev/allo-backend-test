package com.allo.test.modules.finance.loader;

import com.allo.test.modules.finance.client.IDRDataFetcher;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.service.DataStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Unit tests for StartupLoader.
 * <p>
 * Tests the orchestration logic of loading exchange rate data at application startup.
 * All dependencies (WebClient, strategies, DataStoreService) are mocked to test
 * StartupLoader's behavior in isolation.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StartupLoader Unit Tests")
class StartupLoaderTest {

    @Mock
    private WebClient webClient;

    @Mock
    private DataStoreService dataStoreService;

    @Mock
    private IDRDataFetcher latestRatesStrategy;

    @Mock
    private IDRDataFetcher historicalRatesStrategy;

    @Mock
    private IDRDataFetcher currenciesStrategy;

    @Mock
    private ApplicationArguments applicationArguments;

    @InjectMocks
    private StartupLoader startupLoader;

    private List<IDRDataFetcher> strategies;

    @BeforeEach
    void setUp() {
        // Set up mock strategies with their resource types
        when(latestRatesStrategy.getResourceType()).thenReturn(ResourceType.LATEST_RATES);
        when(historicalRatesStrategy.getResourceType()).thenReturn(ResourceType.HISTORICAL_RATES);
        when(currenciesStrategy.getResourceType()).thenReturn(ResourceType.CURRENCIES);

        // Create list of strategies (simulates Spring's auto-injection)
        strategies = Arrays.asList(latestRatesStrategy, historicalRatesStrategy, currenciesStrategy);

        // Manually inject the strategies list since @InjectMocks doesn't handle lists well
        startupLoader = new StartupLoader(strategies, dataStoreService, webClient);
    }

    // ==================== SUCCESS SCENARIOS ====================

    @Test
    @DisplayName("Should load all resources successfully when all strategies fetch data successfully")
    void shouldLoadAllResourcesSuccessfully_WhenAllStrategiesFetchDataSuccessfully() {
        // Arrange
        when(latestRatesStrategy.fetchData(webClient)).thenReturn(List.of(Map.of("key", "value")));
        when(historicalRatesStrategy.fetchData(webClient)).thenReturn(List.of(Map.of("key", "value")));
        when(currenciesStrategy.fetchData(webClient)).thenReturn(List.of(Map.of("key", "value")));
        when(dataStoreService.isDataLoaded()).thenReturn(true);

        // Act
        startupLoader.run(applicationArguments);

        // Assert
        // Verify each strategy's fetchData was called exactly once with the WebClient
        verify(latestRatesStrategy, times(1)).fetchData(webClient);
        verify(historicalRatesStrategy, times(1)).fetchData(webClient);
        verify(currenciesStrategy, times(1)).fetchData(webClient);

        // Verify data loaded check was performed
        verify(dataStoreService, times(1)).isDataLoaded();

        // Verify getResourceType was called for logging
        verify(latestRatesStrategy, atLeastOnce()).getResourceType();
        verify(historicalRatesStrategy, atLeastOnce()).getResourceType();
        verify(currenciesStrategy, atLeastOnce()).getResourceType();
    }

    // ==================== PARTIAL FAILURE SCENARIOS ====================

    @Test
    @DisplayName("Should continue loading when one strategy fails")
    void shouldContinueLoading_WhenOneStrategyFails() {
        // Arrange
        when(latestRatesStrategy.fetchData(webClient)).thenReturn(List.of(Map.of("key", "value")));
        when(historicalRatesStrategy.fetchData(webClient))
                .thenThrow(new RuntimeException("API connection failed"));
        when(currenciesStrategy.fetchData(webClient)).thenReturn(List.of(Map.of("key", "value")));
        when(dataStoreService.isDataLoaded()).thenReturn(false);

        // Act
        startupLoader.run(applicationArguments);

        // Assert
        // Verify ALL strategies were called despite one failing
        verify(latestRatesStrategy, times(1)).fetchData(webClient);
        verify(historicalRatesStrategy, times(1)).fetchData(webClient);
        verify(currenciesStrategy, times(1)).fetchData(webClient);

        // Verify data loaded check was performed
        verify(dataStoreService, times(1)).isDataLoaded();

        // Verify getResourceType was called for the failing strategy (for error logging)
        verify(historicalRatesStrategy, atLeastOnce()).getResourceType();
    }

    @Test
    @DisplayName("Should continue loading when multiple strategies fail")
    void shouldContinueLoading_WhenMultipleStrategiesFail() {
        // Arrange
        when(latestRatesStrategy.fetchData(webClient))
                .thenThrow(new RuntimeException("Latest rates API error"));
        when(historicalRatesStrategy.fetchData(webClient))
                .thenThrow(new RuntimeException("Historical rates API error"));
        when(currenciesStrategy.fetchData(webClient)).thenReturn(List.of(Map.of("key", "value")));
        when(dataStoreService.isDataLoaded()).thenReturn(false);

        // Act
        startupLoader.run(applicationArguments);

        // Assert
        // Verify ALL strategies were called despite multiple failures
        verify(latestRatesStrategy, times(1)).fetchData(webClient);
        verify(historicalRatesStrategy, times(1)).fetchData(webClient);
        verify(currenciesStrategy, times(1)).fetchData(webClient);

        // Verify data loaded check was performed
        verify(dataStoreService, times(1)).isDataLoaded();
    }

    // ==================== COMPLETE FAILURE SCENARIOS ====================

    @Test
    @DisplayName("Should log failure when all strategies fail")
    void shouldLogFailure_WhenAllStrategiesFail() {
        // Arrange
        when(latestRatesStrategy.fetchData(webClient))
                .thenThrow(new RuntimeException("Latest rates failed"));
        when(historicalRatesStrategy.fetchData(webClient))
                .thenThrow(new RuntimeException("Historical rates failed"));
        when(currenciesStrategy.fetchData(webClient))
                .thenThrow(new RuntimeException("Currencies failed"));
        when(dataStoreService.isDataLoaded()).thenReturn(false);

        // Act
        startupLoader.run(applicationArguments);

        // Assert
        // Verify ALL strategies were called
        verify(latestRatesStrategy, times(1)).fetchData(webClient);
        verify(historicalRatesStrategy, times(1)).fetchData(webClient);
        verify(currenciesStrategy, times(1)).fetchData(webClient);

        // Verify data loaded check was performed and returned false
        verify(dataStoreService, times(1)).isDataLoaded();

        // Verify getResourceType was called for error logging
        verify(latestRatesStrategy, atLeastOnce()).getResourceType();
        verify(historicalRatesStrategy, atLeastOnce()).getResourceType();
        verify(currenciesStrategy, atLeastOnce()).getResourceType();
    }
}
