package org.allobank.com.finanacedataservice.strategy;

import org.allobank.com.finanacedataservice.clent.FrankfurterClient;
import org.allobank.com.finanacedataservice.clent.dto.FrankfurterTimeSeriesResponse;
import org.allobank.com.finanacedataservice.domain.HistoricalIdrUsdResult;
import org.allobank.com.finanacedataservice.service.FinanceDataCache;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SupportedCurrenciesFetcherTest {
    @Test
    void loadDataAllCurrenciesInCache() {
        FrankfurterClient client = Mockito.mock(FrankfurterClient.class);
        FinanceDataCache cached = new FinanceDataCache();
        Map<String, String> response = Map.of(
                "USD", "United States Dollar",
                "EUR", "Euro"
        );

        when(client.getSupportedCurrencies()).thenReturn(response);

        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher();
        fetcher.loadData(client, cached);

        List<?> supportedCurrencies = cached.getSupportedCurrencies();
        assertThat(supportedCurrencies).hasSize(2);

        assertThat(supportedCurrencies).extracting("symbol")
                .containsExactlyInAnyOrder("USD", "EUR");

        assertThat(supportedCurrencies).extracting("name")
                .containsExactlyInAnyOrder("United States Dollar", "Euro");
    }

    @Test
    void loadDataHandledNullResponse() {
        FrankfurterClient client = Mockito.mock(FrankfurterClient.class);
        FinanceDataCache cache = new FinanceDataCache();

        when(client.getSupportedCurrencies()).thenReturn(null);

        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher();
        fetcher.loadData(client,cache);
        assertThat(cache.getSupportedCurrencies()).isEmpty();
    }

    @Test
    void loadDataHandleEmptyResponse() {
        FrankfurterClient client = Mockito.mock(FrankfurterClient.class);
        FinanceDataCache cache = new FinanceDataCache();
        when(client.getSupportedCurrencies()).thenReturn(Map.of());
        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher();
        fetcher.loadData(client, cache);

        assertThat(cache.getSupportedCurrencies()).isEmpty();

    }
}
