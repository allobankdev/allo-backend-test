package achlaq.co.allo_backend_test.finance.strategy;

import achlaq.co.allo_backend_test.external.frankfurter.FrankfurterClient;
import achlaq.co.allo_backend_test.finance.model.SupportedCurrenciesView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IdrDataFetcher {

    private final FrankfurterClient client;
    private volatile SupportedCurrenciesView cached;

    @Override
    public void load() {
        Map<String, String> currencies = client.getCurrencies();
        this.cached = new SupportedCurrenciesView(currencies);
    }

    @Override
    public Object getCachedData() {
        return cached;
    }
}

