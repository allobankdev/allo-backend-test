package achlaq.co.allo_backend_test.finance.strategy;

import achlaq.co.allo_backend_test.external.frankfurter.FrankfurterClient;
import achlaq.co.allo_backend_test.finance.model.SupportedCurrenciesView;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SupportedCurrenciesFetcherTest {

    @Test
    void load_shouldCacheCurrencies() {
        FrankfurterClient client = Mockito.mock(FrankfurterClient.class);
        when(client.getCurrencies()).thenReturn(Map.of("USD", "United States Dollar", "IDR", "Indonesian Rupiah"));

        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(client);
        fetcher.load();

        Object cached = fetcher.getCachedData();
        assertThat(cached).isInstanceOf(SupportedCurrenciesView.class);
        SupportedCurrenciesView view = (SupportedCurrenciesView) cached;
        assertThat(view.getCurrencies()).containsKey("USD");
    }
}
