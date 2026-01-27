package allobankdev.test.finance.strategy;

import allobankdev.test.finance.client.FrankfurterClient;
import org.springframework.stereotype.Component;

@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;

    public HistoricalIdrUsdFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String resourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetch() {
        return client.getHistoricalIdrUsd();
    }
}
