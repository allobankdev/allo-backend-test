package achlaq.co.allo_backend_test.finance.strategy;

import achlaq.co.allo_backend_test.external.frankfurter.FrankfurterClient;
import achlaq.co.allo_backend_test.external.frankfurter.dto.HistoricalRatesResponse;
import achlaq.co.allo_backend_test.finance.model.HistoricalIdrUsdView;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class HistoricalIdrUsdFetcherTest {

    @Test
    void load_shouldMapRates() {
        FrankfurterClient client = Mockito.mock(FrankfurterClient.class);
        HistoricalRatesResponse resp = new HistoricalRatesResponse();
        resp.setBase("IDR");
        resp.setStartDate(LocalDate.parse("2024-01-01"));
        resp.setEndDate(LocalDate.parse("2024-01-05"));

        Map<String, Map<String, BigDecimal>> rates = Map.of(
                "2024-01-01", Map.of("USD", new BigDecimal("0.000064")),
                "2024-01-02", Map.of("USD", new BigDecimal("0.000065"))
        );
        resp.setRates(rates);

        when(client.getHistoricalIdrUsd()).thenReturn(resp);

        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(client);
        fetcher.load();

        Object cached = fetcher.getCachedData();
        assertThat(cached).isInstanceOf(HistoricalIdrUsdView.class);
        HistoricalIdrUsdView view = (HistoricalIdrUsdView) cached;
        assertThat(view.getRates()).containsKey("2024-01-01");
    }
}
