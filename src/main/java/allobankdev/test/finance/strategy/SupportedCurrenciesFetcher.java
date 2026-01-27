package allobankdev.test.finance.strategy;

import allobankdev.test.finance.client.FrankfurterClient;
import org.springframework.stereotype.Component;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;

    public SupportedCurrenciesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String resourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetch() {
        return client.getCurrencies();
    }
}

