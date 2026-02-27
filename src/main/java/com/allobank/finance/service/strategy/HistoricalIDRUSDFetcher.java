package com.allobank.finance.service.strategy;

import com.allobank.finance.config.properties.FrankfurterProperties;
import com.allobank.finance.integration.frankfurter.FrankfurterClient;
import com.allobank.finance.model.response.HistoricalRatesResponseBuilder;
import com.allobank.finance.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(Constants.HISTORICAL_IDR_USD)
@RequiredArgsConstructor
public class HistoricalIDRUSDFetcher implements IDRDataFetcher {

    private final FrankfurterProperties frankfurterProperties;
    private final FrankfurterClient client;

    @Override
    public String getResourceType() {
        return Constants.HISTORICAL_IDR_USD;
    }

    @Override
    public Object fetchData() {
        var raw = client.fetchHistoricalRates();
        var rate = frankfurterProperties.resources().historical().to();

        return raw.rates()
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Map<String, Double>>comparingByKey().reversed())
                .map(e -> HistoricalRatesResponseBuilder.builder()
                        .date(e.getKey())
                        .rate(e.getValue().get(rate))
                        .build()
                )
                .toList();
    }
}
