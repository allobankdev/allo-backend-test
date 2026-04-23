package com.allobank.financeaggregator.strategy;

import com.allobank.financeaggregator.dto.LatestIdrRatesDto;
import com.allobank.financeaggregator.exception.ExternalServiceException;
import com.allobank.financeaggregator.model.LatestRatesResponse;
import com.allobank.financeaggregator.service.FrankfurterClient;
import com.allobank.financeaggregator.service.SpreadFactorCalculator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("latest_idr_rates")
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private static final String USD = "USD";

    private final FrankfurterClient client;
    private final SpreadFactorCalculator spreadFactorCalculator;

    public LatestIdrRatesFetcher(FrankfurterClient client, SpreadFactorCalculator spreadFactorCalculator) {
        this.client = client;
        this.spreadFactorCalculator = spreadFactorCalculator;
    }

    @Override
    public LatestIdrRatesDto fetchData() {
        LatestRatesResponse response = client.get("/latest?base=IDR", LatestRatesResponse.class);
        if (response == null || response.rates() == null || !response.rates().containsKey(USD)) {
            throw new ExternalServiceException("USD rate missing in Frankfurter latest response");
        }

        BigDecimal usdRate = response.rates().get(USD);
        BigDecimal spreadFactor = spreadFactorCalculator.getSpreadFactor();
        BigDecimal usdBuySpreadIdr = BigDecimal.ONE
                .divide(usdRate, 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.ONE.add(spreadFactor))
                .setScale(10, RoundingMode.HALF_UP);

        return new LatestIdrRatesDto(
                response.amount(),
                response.base(),
                response.date(),
                Map.copyOf(response.rates()),
                usdBuySpreadIdr
        );
    }
}
