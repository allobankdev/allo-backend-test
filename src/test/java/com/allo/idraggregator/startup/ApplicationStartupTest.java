package com.allo.idraggregator.startup;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.allo.idraggregator.application.service.FinanceDataService;
import com.allo.idraggregator.domain.strategy.IDRDataFetcher;

class ApplicationStartupTest {

    @Mock
    private IDRDataFetcher<String> fetcher1;

    @Mock
    private IDRDataFetcher<String> fetcher2;

    @Mock
    private IDRDataFetcher<String> fetcher3;

    @Mock
    private FinanceDataService service;

    private ApplicationStartup startup;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Map<String, IDRDataFetcher<?>> fetchers = Map.of(
                "latest_idr_rates", fetcher1,
                "historical_idr_usd", fetcher2,
                "supported_currencies", fetcher3);

        startup = new ApplicationStartup(fetchers, service);
    }

    @Test
    void shouldRunAllFetchersAndStoreData() {
        when(fetcher1.fetchData()).thenReturn("fetcher1");
        when(fetcher2.fetchData()).thenReturn("fetcher2");
        when(fetcher3.fetchData()).thenReturn("fetcher3");

        startup.run(null);

        verify(fetcher1).fetchData();
        verify(fetcher2).fetchData();

        verify(service).store("latest_idr_rates", "fetcher1");
        verify(service).store("historical_idr_usd", "fetcher2");
        verify(service).store("supported_currencies", "fetcher3");
    }
}