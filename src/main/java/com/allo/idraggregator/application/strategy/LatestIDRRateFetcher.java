package com.allo.idraggregator.application.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.allo.idraggregator.application.service.SpreadService;
import com.allo.idraggregator.domain.model.LatestRates;
import com.allo.idraggregator.domain.strategy.IDRDataFetcher;
import com.allo.idraggregator.infrastructure.client.FrankfurterClient;

import lombok.RequiredArgsConstructor;

@Component("latest_idr_rates")
@RequiredArgsConstructor
public class LatestIDRRateFetcher implements IDRDataFetcher<LatestRates> {

    private final FrankfurterClient client;
    private final SpreadService spread;

    @Override
    public LatestRates fetchData() {

        LatestRates response = client.getLatestRates("IDR");

        Map<String, Double> rates = response.getRates();

        double spreadValue = spread.getUsdBuySpread(rates.get("USD"));

        return response.toBuilder()
            .usdBuySpreadIdr(spreadValue)
            .build();
    }
}
