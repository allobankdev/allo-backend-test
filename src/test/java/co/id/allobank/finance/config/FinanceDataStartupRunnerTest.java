package co.id.allobank.finance.config;

import co.id.allobank.finance.config.strategy.IDRDataFetcher;
import co.id.allobank.finance.service.InMemoryFinanceStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinanceDataStartupRunnerTest {

    @Mock
    IDRDataFetcher fetcher;

    @Mock
    InMemoryFinanceStore store;

    @Test
    void shouldLoadDataOnStartup() {
        Map<String, IDRDataFetcher> map = Map.of("latest_idr_rates", fetcher);

        when(fetcher.fetchData())
                .thenReturn(List.of("dummy"));

        var runner = new FinanceDataStartupRunner(map, store);

        runner.run(null);

        verify(store).put(eq("latest_idr_rates"), any());
        verify(store).makeImmutable();
    }
}
