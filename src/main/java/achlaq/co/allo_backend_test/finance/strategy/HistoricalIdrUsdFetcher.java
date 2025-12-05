package achlaq.co.allo_backend_test.finance.strategy;

import achlaq.co.allo_backend_test.external.frankfurter.FrankfurterClient;
import achlaq.co.allo_backend_test.external.frankfurter.dto.HistoricalRatesResponse;
import achlaq.co.allo_backend_test.finance.model.HistoricalIdrUsdView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalIdrUsdFetcher implements IdrDataFetcher {

    private final FrankfurterClient client;
    private volatile HistoricalIdrUsdView cached;

    @Override
    public void load() {
        HistoricalRatesResponse response = client.getHistoricalIdrUsd();
        this.cached = new HistoricalIdrUsdView(response.getRates());
    }

    @Override
    public Object getCachedData() {
        return cached;
    }
}

