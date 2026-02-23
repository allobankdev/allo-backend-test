package com.allobank.finance.idrrateaggregator.startegy.impl;

import com.allobank.finance.idrrateaggregator.client.FrankfurterClient;
import com.allobank.finance.idrrateaggregator.model.dto.HistoricalRatesResponse;
import com.allobank.finance.idrrateaggregator.startegy.IDRDataFetcher;
import com.allobank.finance.idrrateaggregator.model.dto.FinanceResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;

    public HistoricalIdrUsdFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public List<FinanceResponse> fetch() {
        HistoricalRatesResponse response = client.getHistoricalIdrUsdRates();

        return response.getRates()
                .entrySet()
                .stream()
                .map(entry -> {
                    String date = entry.getKey();
                    Double usdRate = Optional.ofNullable(entry.getValue().get("USD"))
                            .orElseThrow(() ->
                                    new IllegalStateException("USD rate missing for date " + date)
                            );

                    return new FinanceResponse(date, usdRate);
                })
                .toList();
    }
}
