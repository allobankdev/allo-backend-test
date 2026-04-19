package com.allobankdev.exchangrate.service.strategy.impl;

import com.allobankdev.exchangrate.client.ApiClient;
import com.allobankdev.exchangrate.service.strategy.IdrDataFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LatestIdrRatesFetcher implements IdrDataFetcher {
    private final ApiClient client;
    private final static String TYPE = "latest_idr_rates";
    private final static BigDecimal DIVISOR = BigDecimal.valueOf(100000);

    @Value("${app.github.username}")
    private String githubUsername;

    public LatestIdrRatesFetcher(ApiClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Object fetch() {
        var data =  client.getLatestRates();
        BigDecimal usdRate = data.getRates().get("USD");

        BigDecimal spread = calculateSpread(githubUsername);
        BigDecimal result = BigDecimal.ONE.divide(usdRate, RoundingMode.HALF_UP).multiply(BigDecimal.ONE.add(spread));
        data.setUsdBuySpreadIdr(result);

        return data;
    }

    private BigDecimal calculateSpread(String username) {
        int sum = username.chars().sum();
        int mod = sum % 1000;

        return BigDecimal.valueOf(mod)
                .divide(DIVISOR, 6, RoundingMode.HALF_UP);
    }
}
