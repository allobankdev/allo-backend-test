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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class HistoricalIdrUsdFetcherTest {

    @Test
    void loadDataFiltersAndStoresUsdDataInCache() {
        FrankfurterClient client = Mockito.mock(FrankfurterClient.class);
        FinanceDataCache cache = new FinanceDataCache();

        FrankfurterTimeSeriesResponse response = new FrankfurterTimeSeriesResponse();
        response.setBase("IDR");
        response.setRates(Map.of(
                LocalDate.parse("2024-01-01"), Map.of("USD", new BigDecimal("0.000064")),
                LocalDate.parse("2024-01-02"), Map.of("EUR", new BigDecimal("0.000060"))
        ));

        when(client.getHistoricalIdrUsd()).thenReturn(response);
        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher();
        fetcher.loadData(client, cache);

        List<?> cached = cache.getHistoricalIdrUsd();
        assertThat(cached).hasSize(1);
        Object element = cached.get(0);
        assertThat(element).isInstanceOf(HistoricalIdrUsdResult.class);

        HistoricalIdrUsdResult result = (HistoricalIdrUsdResult) element;
        assertThat(result.getDate()).isEqualTo("2024-01-01");
        assertThat(result.getUsdRate()).isEqualByComparingTo("0.000064");
    }

    @Test
    void loadDataHandledNullResponse() {
        FrankfurterClient client = Mockito.mock(FrankfurterClient.class);
        FinanceDataCache cache = new FinanceDataCache();

        when(client.getHistoricalIdrUsd()).thenReturn(null);
        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher();
        fetcher.loadData(client, cache);
        assertThat(cache.getHistoricalIdrUsd()).isEmpty();
    }

    @Test
    void loadDataHandledEmptyRates() {
        FrankfurterClient client = Mockito.mock(FrankfurterClient.class);
        FinanceDataCache cache = new FinanceDataCache();

        FrankfurterTimeSeriesResponse response = new FrankfurterTimeSeriesResponse();
        response.setRates(Map.of());
        when(client.getHistoricalIdrUsd()).thenReturn(response);

        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher();
        fetcher.loadData(client, cache);
        assertThat(cache.getHistoricalIdrUsd()).isEmpty();
    }
}