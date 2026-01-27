package allobankdev.test.finance.strategy;

import allobankdev.test.finance.client.FrankfurterClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private static final String USERNAME = "hamramaputra17";

    private final FrankfurterClient client;

    public LatestIdrRatesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String resourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetch() {
        Map<String, Object> data = client.getLatestIdrRates();
        Map<String, Object> rates = (Map<String, Object>) data.get("rates");

        double rateUsd = ((Number) rates.get("USD")).doubleValue();

        int sum = USERNAME.toLowerCase().chars().sum();
        double spread = (sum % 1000) / 100000.0;

        double usdBuySpreadIdr = (1 / rateUsd) * (1 + spread);

        data.put("USD_BuySpread_IDR", usdBuySpreadIdr);
        data.put("spreadFactor", spread);

        return data;
    }

}


