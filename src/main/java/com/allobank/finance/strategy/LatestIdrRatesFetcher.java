package com.allobank.finance.strategy;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.config.FinanceProperties;
import com.allobank.finance.dto.LatestRateResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {
    private final FrankfurterClient frankfurterClient;
    private final FinanceProperties financeProperties;

    public LatestIdrRatesFetcher(FrankfurterClient frankfurterClient, FinanceProperties financeProperties) {
        this.frankfurterClient = frankfurterClient;
        this.financeProperties = financeProperties;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    public Object fetch() {

        LatestRateResponse latestRateResponse = frankfurterClient.getLatestIdrRates();

        BigDecimal usdRate = latestRateResponse.getRate().get("USD");

        double spreadFactor = calculateSpread(financeProperties.getGithubUsername());

        double usdAfterSpread = (1 / usdRate.doubleValue()) * (1 + spreadFactor);

        LatestRateResponse result = new LatestRateResponse();

        result.setBaseCurrency(latestRateResponse.getBaseCurrency());
        result.setDate(latestRateResponse.getDate());
        result.setRate(latestRateResponse.getRate());
        result.setUsdSpreadIdr(usdAfterSpread);

        return result;
    }

    private double calculateSpread(String username) {

        int sum = username.toLowerCase().chars().sum();

        return (sum % 1000) / 100000.0;
    }
}
