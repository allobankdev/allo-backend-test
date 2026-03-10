package com.allobank.financeapi.runner;

import com.allobank.financeapi.FinanceApiApplication;
import com.allobank.financeapi.model.enums.ResourceType;
import com.allobank.financeapi.service.FinanceDataService;
import com.allobank.financeapi.service.strategy.DataFetcherStrategy;
import com.allobank.financeapi.service.strategy.HistoricalDataStrategy;
import com.allobank.financeapi.service.strategy.LatestRatesStrategy;
import com.allobank.financeapi.service.strategy.SupportedCurrenciesStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = FinanceApiApplication.class)
class DataInitializerTest {

    @Autowired
    private FinanceDataService financeDataService;

    @MockBean
    private LatestRatesStrategy latestRatesStrategy;

    @MockBean
    private HistoricalDataStrategy historicalDataStrategy;

    @MockBean
    private SupportedCurrenciesStrategy supportedCurrenciesStrategy;

    @Autowired
    private DataInitializer dataInitializer;

    @Test
    void run_initializesDataSuccessfully() throws Exception {
        // Clear the data and reset immutability since it already ran during startup
        financeDataService.clearData();

        when(latestRatesStrategy.getResourceType()).thenReturn(ResourceType.LATEST_IDR_RATES);
        when(latestRatesStrategy.fetchData()).thenReturn(Mono.just("latest_rates_data"));

        when(historicalDataStrategy.getResourceType()).thenReturn(ResourceType.HISTORICAL_IDR_USD);
        when(historicalDataStrategy.fetchData()).thenReturn(Mono.just("historical_data"));

        when(supportedCurrenciesStrategy.getResourceType()).thenReturn(ResourceType.SUPPORTED_CURRENCIES);
        when(supportedCurrenciesStrategy.fetchData()).thenReturn(Mono.just("supported_currencies_data"));

        // Manually trigger the runner since it already ran during startup with unconfigured mocks
        dataInitializer.run(null);

        // Allow some time for the async operations to complete
        Thread.sleep(1000);

        assertTrue(financeDataService.getData(ResourceType.LATEST_IDR_RATES).isPresent());
        assertTrue(financeDataService.getData(ResourceType.HISTORICAL_IDR_USD).isPresent());
        assertTrue(financeDataService.getData(ResourceType.SUPPORTED_CURRENCIES).isPresent());
    }
}
