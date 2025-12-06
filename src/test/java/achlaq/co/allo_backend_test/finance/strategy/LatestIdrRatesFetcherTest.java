package achlaq.co.allo_backend_test.finance.strategy;

import achlaq.co.allo_backend_test.external.frankfurter.FrankfurterClient;
import achlaq.co.allo_backend_test.external.frankfurter.dto.LatestRatesResponse;
import achlaq.co.allo_backend_test.finance.model.LatestIdrRatesView;
import achlaq.co.allo_backend_test.config.FrankfurterProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class LatestIdrRatesFetcherTest {

    FrankfurterClient client = Mockito.mock(FrankfurterClient.class);
    FrankfurterProperties props = new FrankfurterProperties();

    @BeforeEach
    void setup() {
        props.setGithubUsername("achlaq");
    }

    @Test
    void load_shouldComputeUsdBuySpreadIdr() {
        LatestRatesResponse resp = new LatestRatesResponse();
        resp.setBase("IDR");
        resp.setDate(LocalDate.of(2024,1,1));
        resp.setAmount(BigDecimal.ONE);
        resp.setRates(Map.of("USD", new BigDecimal("0.000064")));
        when(client.getLatestIdrRates()).thenReturn(resp);

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(client, props);
        fetcher.load();

        Object cached = fetcher.getCachedData();
        assertThat(cached).isInstanceOf(LatestIdrRatesView.class);
        LatestIdrRatesView view = (LatestIdrRatesView) cached;

        assertThat(view.getRates()).containsKey("USD");
        assertThat(view.getUsdBuySpreadIdr()).isNotNull();
    }
}
