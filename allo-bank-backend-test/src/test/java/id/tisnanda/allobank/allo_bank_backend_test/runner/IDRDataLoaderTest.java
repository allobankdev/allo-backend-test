package id.tisnanda.allobank.allo_bank_backend_test.runner;

import id.tisnanda.allobank.allo_bank_backend_test.service.IDRFinanceService;
import id.tisnanda.allobank.allo_bank_backend_test.strategy.IDRDataFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class IDRDataLoaderTest {

    private IDRFinanceService financeService;
    private IDRDataFetcher fetcherMock;
    private IDRDataLoader loader;

    @BeforeEach
    void setUp() {
        financeService = mock(IDRFinanceService.class);
        fetcherMock = mock(IDRDataFetcher.class);

        Map<String, IDRDataFetcher> fetchers = Map.of("historical_idr_usd", fetcherMock);
        loader = new IDRDataLoader(financeService, fetchers);
    }

    @Test
    void testRun_success() throws Exception {
        List<Map<String, Object>> mockData = Collections.singletonList(Map.of("USD", 0.00006));
        when(fetcherMock.fetchData()).thenReturn(mockData);

        loader.run(null);

        verify(fetcherMock, times(1)).fetchData();
        verify(financeService, times(1)).setData("historical_idr_usd", mockData);
    }

    @Test
    void testRun_fetcherThrows_exception() throws Exception {
        when(fetcherMock.fetchData()).thenThrow(new RuntimeException("API failed"));

        try {
            loader.run(null);
        } catch (IllegalStateException ignored) {}

        verify(fetcherMock, times(1)).fetchData();
        verify(financeService, never()).setData(anyString(), any());
    }
}
