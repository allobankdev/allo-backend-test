package com.allobank.finance.service.strategy;

import com.allobank.finance.config.properties.FrankfurterProperties;
import com.allobank.finance.integration.frankfurter.FrankfurterClient;
import com.allobank.finance.model.response.LatestRatesResponseBuilder;
import com.allobank.finance.util.Constants;
import com.allobank.finance.util.SpreadCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component(Constants.LATEST_IDR_RATES)
@RequiredArgsConstructor
@EnableConfigurationProperties(FrankfurterProperties.class)
public class LatestIDRRatesFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;
    private final SpreadCalculator spreadCalculator;

    @Override
    public String getResourceType() {
        return Constants.LATEST_IDR_RATES;
    }

    @Override
    public Object fetchData() {
        var raw = client.fetchLatestRates();
        double spread = spreadCalculator.calculateSpreadFactor();

        return raw.rates()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    String currency = e.getKey();
                    double rate = e.getValue();

                    double buySpread = (1 / rate) * (1 + spread);

                    Map<String, Object> dynamic = Map.of(
                            currency.toLowerCase(Locale.ROOT) + "_buy_spread_idr", buySpread
                    );

                    return LatestRatesResponseBuilder.builder()
                            .currency(currency)
                            .rate(rate)
                            .dynamicFields(dynamic)
                            .build();
                })
                .toList();
    }
}
